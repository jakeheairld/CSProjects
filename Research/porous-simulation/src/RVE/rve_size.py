# Author: Jake Heairld
# Last Updated: 3/12/25
#
# Description: This script generates homogeneous circular porous meshes
# to test mesh dependency (mesh sensitivity) for RVE analysis paper.
import gmsh

L0 = 2
plane_width = 10
plane_height = 10
lc = 0.2 / L0
cm = 1 / L0

# Initialize gmsh
gmsh.initialize()

# Function to create the base mesh
def create_unit_cell(x_offset=0, y_offset=0, tag_offset=0):
    # Define the square boundary
    p1 = gmsh.model.geo.addPoint(x_offset, y_offset, 0, cm, tag_offset + 1)
    p2 = gmsh.model.geo.addPoint(x_offset + plane_width, y_offset, 0, cm, tag_offset + 2)
    p3 = gmsh.model.geo.addPoint(x_offset + plane_width, y_offset + plane_height, 0, cm, tag_offset + 3)
    p4 = gmsh.model.geo.addPoint(x_offset, y_offset + plane_height, 0, cm, tag_offset + 4)

    l1 = gmsh.model.geo.addLine(p1, p2, tag_offset + 1)
    l2 = gmsh.model.geo.addLine(p2, p3, tag_offset + 2)
    l3 = gmsh.model.geo.addLine(p3, p4, tag_offset + 3)
    l4 = gmsh.model.geo.addLine(p4, p1, tag_offset + 4)

    outer_loop = gmsh.model.geo.addCurveLoop([l1, l2, l3, l4], tag_offset + 1)

    # Define circular pore
    pore_x, pore_y, pore_r = x_offset + 5.0, y_offset + 5.0, 2.0
    left = gmsh.model.geo.addPoint(pore_x - pore_r, pore_y, 0, lc, tag_offset + 5)
    center = gmsh.model.geo.addPoint(pore_x, pore_y, 0, lc, tag_offset + 6)
    right = gmsh.model.geo.addPoint(pore_x + pore_r, pore_y, 0, lc, tag_offset + 7)

    arc1 = gmsh.model.geo.addCircleArc(left, center, right, tag_offset + 5)
    arc2 = gmsh.model.geo.addCircleArc(right, center, left, tag_offset + 6)
    inner_loop = gmsh.model.geo.addCurveLoop([arc1, arc2], tag_offset + 2)

    # Create the surface
    surface = gmsh.model.geo.addPlaneSurface([outer_loop, inner_loop])
    return surface

# Create a 2x2 RVE
num_cells_x = 2
num_cells_y = 2
tag_offset = 0
for i in range(num_cells_x):
    for j in range(num_cells_y):
        create_unit_cell(i * plane_width, j * plane_height, tag_offset)
        tag_offset += 10  # Ensure unique tags

gmsh.model.geo.synchronize()
gmsh.model.mesh.generate(2)

# Save the mesh
gmsh.write(f"rve_meshes/mesh2_2x2.msh")

# Clear and create a 3x3 version
gmsh.clear()
gmsh.model.geo.synchronize()

num_cells_x = 3
num_cells_y = 3
tag_offset = 0
for i in range(num_cells_x):
    for j in range(num_cells_y):
        create_unit_cell(i * plane_width, j * plane_height, tag_offset)
        tag_offset += 10  # Ensure unique tags

gmsh.model.geo.synchronize()
gmsh.model.mesh.generate(2)

# Save the mesh
gmsh.write(f"rve_meshes/mesh2_3x3.msh")

gmsh.finalize()
