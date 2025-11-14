#!/usr/bin/env python
# coding: utf-8

import numpy as np
import ufl
from mpi4py import MPI
import gmsh
from petsc4py import PETSc
from dolfinx import plot, io, fem, mesh, default_scalar_type
import dolfinx_mpc.utils
import dolfinx_mpc as dmpc
from dolfinx.io import XDMFFile, gmshio

import meshio
import math
import sys
import pyvista
import os

# Set visualization settings appropriate for headless/PyCharm environment
pyvista.OFF_SCREEN = True  # Force off-screen rendering
os.environ["PYVISTA_OFF_SCREEN"] = "true"  # Environment variable for off-screen

# Dimension of the problem
gdim = 2

# Read mesh
try:
    domain, cell_markers, facet_markers = gmshio.read_from_msh("RVE/meshes/xdmf_meshes/mesh1.msh", MPI.COMM_WORLD, gdim=gdim)
    print("Successfully loaded mesh")
except Exception as e:
    print(f"Error loading mesh: {e}")
    sys.exit(1)

# Save initial mesh visualization to file instead of displaying
p = pyvista.Plotter(off_screen=True)
topology, cell_types, geometry = plot.vtk_mesh(domain)
grid = pyvista.UnstructuredGrid(topology, cell_types, geometry)
p.add_mesh(grid, show_edges=True)
p.show_axes()
p.screenshot("mesh.png")
print("Saved initial mesh visualization to mesh.png")

# Get cells for different markers
cells_0 = cell_markers.find(1)
cells_1 = cell_markers.find(2)

# Visualize subdomains and save to file
num_cells_local = domain.topology.index_map(domain.topology.dim).size_local
marker = np.zeros(num_cells_local, dtype=np.int32)
cells_0 = cells_0[cells_0 < num_cells_local]
cells_1 = cells_1[cells_1 < num_cells_local]
marker[cells_0] = 1
marker[cells_1] = 2
topology, cell_types, x = plot.vtk_mesh(domain, domain.topology.dim, np.arange(num_cells_local, dtype=np.int32))

p = pyvista.Plotter(off_screen=True, window_size=[800, 800])
grid = pyvista.UnstructuredGrid(topology, cell_types, x)
grid.cell_data["Marker"] = marker
grid.set_active_scalars("Marker")
p.add_mesh(grid, show_edges=True)
p.screenshot("subdomains_structured.png")
print("Saved subdomain visualization to subdomains_structured.png")


# Create piecewise constant field
def create_piecewise_constant_field(domain, cell_markers, property_dict, name=None):
    """Create a piecewise constant field with different values per subdomain."""
    V0 = fem.functionspace(domain, ("DG", 0))
    k = fem.Function(V0, name=name)
    for tag, value in property_dict.items():
        cells = cell_markers.find(tag)
        k.x.array[cells] = np.full_like(cells, value, dtype=np.float64)
    return k


# Material properties
E_mat = 3.45e9  # Young's modulus for matrix
nu_mat = 0.35  # Poisson's ratio for matrix
E_fiber = 1.8e9  # Young's modulus for fiber
nu_fiber = 0.43  # Poisson's ratio for fiber

# Create fields for material properties
E = create_piecewise_constant_field(
    domain, cell_markers, {1: E_mat, 2: E_fiber}, name="YoungModulus"
)
nu = create_piecewise_constant_field(
    domain, cell_markers, {1: nu_mat, 2: nu_fiber}, name="PoissonRatio"
)

# Compute Lamé parameters
lmbda = E * nu / (1 + nu) / (1 - 2 * nu)
mu = E / 2 / (1 + nu)

# Get domain boundaries
xface = np.max(domain.geometry.x[:, 0])
xface_neg = np.min(domain.geometry.x[:, 0])
yface = np.max(domain.geometry.x[:, 1])
yface_neg = np.min(domain.geometry.x[:, 1])
dx = xface - xface_neg
dy = yface - yface_neg
v_total = dx * dy
vol = fem.assemble_scalar(fem.form(1 * ufl.dx(domain=domain)))

# Define boundaries
boundaries = [(2, lambda x: np.isclose(x[0], xface)),
              (3, lambda x: np.isclose(x[1], yface)),
              (0, lambda x: np.isclose(x[0], xface_neg)),
              (1, lambda x: np.isclose(x[1], yface_neg))]

# Create boundary tags
facet_indices, facet_markers = [], []
tdim = domain.topology.dim
fdim = tdim - 1
domain.topology.create_connectivity(fdim, tdim)
for (marker, locator) in boundaries:
    facets = mesh.locate_entities(domain, fdim, locator)
    facet_indices.append(facets)
    facet_markers.append(np.full_like(facets, marker))
facet_indices = np.hstack(facet_indices).astype(np.int32)
facet_markers = np.hstack(facet_markers).astype(np.int32)
sorted_facets = np.argsort(facet_indices)
facet_tag = mesh.meshtags(domain, fdim, facet_indices[sorted_facets], facet_markers[sorted_facets])
ds = ufl.Measure("ds", domain=domain, subdomain_data=facet_tag)
n = ufl.FacetNormal(domain)

# Define domain corners and periodicity vectors
corners = np.array([[xface_neg, yface_neg], [xface, yface_neg], [xface, yface], [xface_neg, yface]])
a1 = corners[1, :] - corners[0, :]  # first vector generating periodicity
a2 = corners[3, :] - corners[0, :]  # second vector generating periodicity

# Define strain and stress
Eps = fem.Constant(domain, np.zeros((2, 2)))
Eps_ = fem.Constant(domain, np.zeros((2, 2)))
y = ufl.SpatialCoordinate(domain)


def epsilon(v):
    return ufl.sym(ufl.grad(v))


def sigma(v):
    eps = Eps + epsilon(v)
    return lmbda * ufl.tr(eps) * ufl.Identity(gdim) + 2 * mu * eps


# Create function space and forms
V = fem.functionspace(domain, ("P", 2, (gdim,)))
du = ufl.TrialFunction(V)
u_ = ufl.TestFunction(V)
a_form, L_form = ufl.system(ufl.inner(sigma(du), epsilon(u_)) * ufl.dx)

# Define boundary conditions
point_dof = fem.locate_dofs_geometrical(
    V, lambda x: np.isclose(x[0], xface_neg) & np.isclose(x[1], yface_neg)
)
bcs = [fem.dirichletbc(np.zeros((gdim,)), point_dof, V)]


# Define periodicity relations
def periodic_relation_left_right(x):
    out_x = np.zeros(x.shape)
    out_x[0] = x[0] - a1[0]
    out_x[1] = x[1] - a1[1]
    out_x[2] = x[2]
    return out_x


def periodic_relation_bottom_top(x):
    out_x = np.zeros(x.shape)
    out_x[0] = x[0] - a2[0]
    out_x[1] = x[1] - a2[1]
    out_x[2] = x[2]
    return out_x


# Create multi-point constraints
mpc = dolfinx_mpc.MultiPointConstraint(V)
mpc.create_periodic_constraint_topological(
    V, facet_tag, 2, periodic_relation_left_right, bcs
)
mpc.create_periodic_constraint_topological(
    V, facet_tag, 3, periodic_relation_bottom_top, bcs
)
mpc.finalize()

# Setup problem
u = fem.Function(mpc.function_space, name="Displacement")
v = fem.Function(mpc.function_space, name="Periodic_fluctuation")
problem = dmpc.LinearProblem(
    a_form,
    L_form,
    mpc,
    bcs=bcs,
    u=v,
    petsc_options={"ksp_type": "preonly", "pc_type": "lu"},
)


# Function to save warped plots instead of displaying them
def save_warped_plot(u, scale=1.0, plot_mesh=False, filename=None, title=None):
    Vu = u.function_space
    u_topology, u_cell_types, u_geometry = plot.vtk_mesh(Vu)
    u_grid = pyvista.UnstructuredGrid(u_topology, u_cell_types, u_geometry)
    u_3D = np.zeros((u_geometry.shape[0], 3))
    u_3D[:, :2] = u.x.array.reshape(-1, 2)
    u_grid.point_data[u.name] = u_3D
    u_grid.set_active_vectors(u.name)
    warped = u_grid.warp_by_vector(u.name, factor=scale)

    plotter = pyvista.Plotter(off_screen=True)
    plotter.window_size = (800, 300)
    plotter.add_mesh(warped)
    if plot_mesh:
        edges = warped.extract_all_edges()
        plotter.add_mesh(edges, color="k", line_width=1, opacity=0.5)
    plotter.view_xy()
    if title:
        plotter.add_text(title, font_size=14)

    if filename:
        plotter.screenshot(filename)
        print(f"Saved warped plot to {filename}")
    else:
        plotter.screenshot(f"warped_plot.png")
        print("Saved warped plot to warped_plot.png")


# Define elementary load cases
elementary_load = [
    np.array([[1.0, 0.0], [0.0, 0.0]]),
    np.array([[0.0, 0.0], [0.0, 1.0]]),
    np.array([[0.0, 0.5], [0.5, 0.0]]),
]
load_labels = ["Exx", "Eyy", "Exy"]
dim_load = len(elementary_load)

# Solve for each load case
C_hom = np.zeros((dim_load, dim_load))
for nload in range(dim_load):
    print(f"Processing load case {nload + 1}/{dim_load}: {load_labels[nload]}")
    Eps.value = elementary_load[nload]
    u.interpolate(
        fem.Expression(
            ufl.dot(Eps, y), mpc.function_space.element.interpolation_points()
        )
    )

    problem.solve()
    u.x.array[:] += v.x.array[:]

    # Save visualization instead of displaying
    save_warped_plot(u, scale=0.5, plot_mesh=True,
                     filename=f"warped_{load_labels[nload]}.png",
                     title=load_labels[nload])

    for nload_ in range(dim_load):
        Eps_.value = elementary_load[nload_]
        C_hom[nload, nload_] = (
                fem.assemble_scalar(fem.form(ufl.inner(sigma(v), Eps_) * ufl.dx)) / vol
        )

# Print results
print("\nHomogenized stiffness tensor C_hom:")
print(C_hom)
print("\nCalculations complete!")

# Save results to file
np.savetxt("C_hom_results.txt", C_hom)
print("Results saved to C_hom_results.txt")