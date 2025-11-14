# Author: Jake Heairld
# Last Updated: 3/12/25
#
# Description: This script generates homogeneous circular porous meshes
# to test mesh dependency (mesh sensitivity) for RVE analysis paper.

import gmsh
import os
import sys

def generate_mesh(L0=1):
    plane_width = 10
    plane_height = 10
    lc = 5 / L0
    cm = 10 / L0

    # Initialize gmsh
    gmsh.initialize()
    gmsh.model.add(f"L0_{L0}")

    # Create a square uniform mesh with uniform mesh size
    gmsh.model.geo.addPoint(0, 0, 0, cm, 1)
    gmsh.model.geo.addPoint(plane_width, 0, 0, cm, 2)
    gmsh.model.geo.addPoint(plane_width, plane_height, 0, cm, 3)
    gmsh.model.geo.addPoint(0, plane_height, 0, cm, 4)
    gmsh.model.geo.addLine(1, 2, 1)
    gmsh.model.geo.addLine(3, 2, 2)
    gmsh.model.geo.addLine(3, 4, 3)
    gmsh.model.geo.addLine(4, 1, 4)
    gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

    # Initialize list to store curve loops for the main surface and pores
    curve_loops = [1]

    # Define pores as circles (disks)
    class Pore:
        def __init__(self, x, y, r):
            self.x = x
            self.y = y
            self.r = r

    pore = Pore(5.0, 5.0, 2.0)

    left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, lc)
    center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, lc)
    right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, lc)

    gmsh.model.geo.addCircleArc(left, center, right, 7)
    gmsh.model.geo.addCircleArc(right, center, left, 8)
    gmsh.model.geo.addCurveLoop([7, 8], 9)

    # Append each pore curve loop to the curve_loops list
    curve_loops.append(9)

    # Create the surface
    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)

    # Synchronize to apply changes to the model
    gmsh.model.geo.synchronize()

    # Add physical groups for surfaces and boundaries after synchronization
    gmsh.model.addPhysicalGroup(2, [surface_tag], tag=1)  # Physical surface group for the entire domain
    gmsh.model.addPhysicalGroup(1, [1, 2, 3, 4], tag=2)  # Physical boundary group for edges

    # Generate the mesh
    gmsh.model.mesh.generate(2)

    # Define output directory and save
    mesh_dir = os.path.join("meshes", f"L0_{L0}")
    os.makedirs(mesh_dir, exist_ok=True)
    gmsh.write(os.path.join(mesh_dir, "mesh1.msh"))

    # Clear the current model to free up memory for the next one
    gmsh.clear()

    # Finalize gmsh
    gmsh.finalize()

if __name__ == "__main__":
    L0 = float(sys.argv[1]) if len(sys.argv) > 1 else 1.0
    generate_mesh(L0)