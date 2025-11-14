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

# Define pores as circles (disks)
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r

pore = Pore(5.0, 5.0, 2.0)

left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

gmsh.model.geo.addCircleArc(left, center, right, 7)
gmsh.model.geo.addCircleArc(right, center, left, 8)
gmsh.model.geo.addCurveLoop([7, 8], 9)

# Append each pore curve loop to the curve_loops list
curve_loops.append(9)

surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
gmsh.model.geo.synchronize()

gmsh.model.mesh.generate(2)
gmsh.write(f"Morphologies/CircleMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()