import gmsh
import math

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


# Ellipse Pore Class
class Pore:
    def __init__(self, x, y, a, b):
        self.x = x  # x-coordinate of the center
        self.y = y  # y-coordinate of the center
        self.a = a  # semi-major axis
        self.b = b  # semi-minor axis


# Function to create an elliptical pore using parametric equations
def create_ellipse_pore(pore, num_points=10):

    points = []
    for i in range(num_points):
        t = 2 * math.pi * i / num_points  # parameter t from 0 to 2pi
        x = pore.x + pore.a * math.cos(t)
        y = pore.y + pore.b * math.sin(t)
        points.append(gmsh.model.geo.addPoint(x, y, 0, cm))

    # Create the elliptical curve by connecting the points
    curve_loop = []
    for i in range(num_points):
        curve_loop.append(
            gmsh.model.geo.addLine(points[i], points[(i + 1) % num_points]))  # Wrap around to close the loop

    return gmsh.model.geo.addCurveLoop(curve_loop)


# Create an elliptical pore at the center of the domain
ellipse_pore = Pore(5.0, 5.0, 2.0, 1.0)  # Example: center (5,5), semi-major axis 2, semi-minor axis 1

# Create the elliptical pore and get the curve loop
ellipse_curve_loop = create_ellipse_pore(ellipse_pore)

# Add the ellipse pore curve loop to the surface
curve_loops.append(ellipse_curve_loop)

# Create the main surface (square mesh)
surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)

# Synchronize and generate the mesh
gmsh.model.geo.synchronize()
gmsh.model.mesh.generate(2)

# Write the mesh to file
gmsh.write(f"Morphologies/EllipseMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()