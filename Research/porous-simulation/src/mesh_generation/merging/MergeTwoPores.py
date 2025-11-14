import gmsh
import math


# Define pores as circles (disks)
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r


plane_width = 10
plane_height = 10
cm = 0.1  # general mesh size
lc_fine = 0.05  # finer mesh size for the pores
lc = 0.5  # regular mesh size

# Initialize gmsh
gmsh.initialize()

# Create the square surface (mesh)
gmsh.model.occ.addPoint(0, 0, 0, lc, 1)
gmsh.model.occ.addPoint(plane_width, 0, 0, lc, 2)
gmsh.model.occ.addPoint(plane_width, plane_height, 0, lc, 3)
gmsh.model.occ.addPoint(0, plane_height, 0, lc, 4)
gmsh.model.occ.addLine(1, 2, 1)
gmsh.model.occ.addLine(2, 3, 2)
gmsh.model.occ.addLine(3, 4, 3)
gmsh.model.occ.addLine(4, 1, 4)
gmsh.model.occ.addCurveLoop([1, 2, 3, 4], 1)

# Initialize list to store curve loops for the main surface and pores
curve_loop = [1]

# Create the square plane surface
gmsh.model.occ.addPlaneSurface(curve_loop, 1)

# List of pores (holes)
pores = [Pore(4.0, 5.0, 2.0), Pore(6.0, 5.0, 2.0), Pore(5, 9, 0.5)]  # Example three pores
pore_surfaces = []

# Create each pore (circle) surface
for i, pore in enumerate(pores, start=1):
    # Define the center and points of the circles
    center = gmsh.model.occ.addPoint(pore.x, pore.y, 0, lc_fine)
    left = gmsh.model.occ.addPoint(pore.x - pore.r, pore.y, 0, lc_fine)
    right = gmsh.model.occ.addPoint(pore.x + pore.r, pore.y, 0, lc_fine)

    # Calculate intersection points of upper and lower arcs (if applicable)
    # Intersection points can be determined by geometry or approximation.
    # We'll use a simple assumption for now:
    # Upper and lower arcs meet at the top and bottom of the pore at specific points
    intersection_upper = gmsh.model.occ.addPoint(pore.x, pore.y + pore.r, 0, lc_fine)
    intersection_lower = gmsh.model.occ.addPoint(pore.x, pore.y - pore.r, 0, lc_fine)

    # Create circle arcs
    arc_upper = gmsh.model.occ.addCircleArc(left, intersection_upper, right)
    arc_lower = gmsh.model.occ.addCircleArc(right, intersection_lower, left)

    # Create pore loop
    pore_loop = gmsh.model.occ.addCurveLoop([arc_upper, arc_lower])
    pore_surface = gmsh.model.occ.addPlaneSurface([pore_loop], i + 1)
    pore_surfaces.append(pore_surface)

# Subtract the pores (circles) from the square one by one
for i, pore_surface in enumerate(pore_surfaces):
    gmsh.model.occ.cut([(2, 1)], [(2, pore_surface)])

# Synchronize to apply changes
gmsh.model.occ.synchronize()

# Create the mesh
gmsh.model.mesh.generate(2)

# Write the mesh to file
gmsh.write("MergedPores.msh")

# Show the GUI for viewing
gmsh.fltk.run()

# Clear the current model to free up memory for the next one
gmsh.clear()

# Finalize gmsh
gmsh.finalize()
