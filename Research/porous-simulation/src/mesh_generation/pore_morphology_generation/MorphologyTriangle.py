import gmsh

plane_width = 10
plane_height = 10
cm = 0.1
lc = 0.5

# Initialize gmsh
gmsh.initialize()

# Create a square uniform mesh with uniform mesh size
gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
gmsh.model.geo.addLine(1, 2, 1)
gmsh.model.geo.addLine(3, 2, 2)
gmsh.model.geo.addLine(3, 4, 3)
gmsh.model.geo.addLine(4, 1, 4)
gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

# Initialize list to store curve loops for the main surface and pores
curve_loops = [1]

# Triangle Pore Class
class Pore:
    def __init__(self, x, y, l):
        self.x = x  # Center x-coordinate
        self.y = y  # Center y-coordinate
        self.l = l  # Side length

pore = Pore(5.0, 5.0, 3.0)

# Calculate half the side length and the height of the equilateral triangle
half_side = pore.l / 2
height = (3 ** 0.5) * half_side  # height of an equilateral triangle

# Define the three points of the equilateral triangle
p1 = gmsh.model.geo.addPoint(pore.x, pore.y + height / 2, 0, cm)
p2 = gmsh.model.geo.addPoint(pore.x - half_side, pore.y - height / 2, 0, cm)
p3 = gmsh.model.geo.addPoint(pore.x + half_side, pore.y - height / 2, 0, cm)

# Define the lines for the triangle
l1 = gmsh.model.geo.addLine(p1, p2)
l2 = gmsh.model.geo.addLine(p2, p3)
l3 = gmsh.model.geo.addLine(p3, p1)

# Create a curve loop for the triangle
gmsh.model.geo.addCurveLoop([l1, l2, l3], 9)

# Append each pore curve loop to the curve_loops list
curve_loops.append(9)

surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
gmsh.model.geo.synchronize()

gmsh.model.mesh.generate(2)
gmsh.write(f"Morphologies/TriangleMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()