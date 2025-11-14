import gmsh
import matplotlib.pyplot as plt

plane_width = 10
plane_height = 10
cm = 0.1
lc = 0.5

# Initialize gmsh
gmsh.initialize()

# Square Pore Class
class Point:
    def __init__(self, x, y):
        self.x = x  # x-coordinate
        self.y = y  # y-coordinate

p1 = Point(5, 7.8)
p2 = Point(5.8, 7)
p3 = Point(7, 4)
p4 = Point(7.1, 3)
p5 = Point(6.9, 2.3)
p6 = Point(6.5, 2.4)
p7 = Point(5.9, 2.8)
p8 = Point(5, 3.2)
p9 = Point(4.1, 2.8)
p10 = Point(3.5, 2.4)
p11 = Point(3.1, 2.3)
p12 = Point(2.9, 3)
p13 = Point(3, 4)
p14 = Point(4.2, 7)
points = [p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14]

# Collect coordinates for points p1 to p12
point_coords = []

# Get coordinates for each point using gmsh.model.geo.getPoint
for point in points:
    point_coords.append((point.x, point.y))

# Plot the points p1 to p12 using matplotlib
x_coords, y_coords = zip(*point_coords)
plt.figure(figsize=(8, 8))
plt.scatter(x_coords, y_coords, color='blue', label='Points p1 to p12')
plt.title('Rounded Arrow Shape (Points p1 to p12)')
plt.xlabel('X')
plt.ylabel('Y')

# Set up the grid with an origin at (0,0)
plt.xlim(-1, plane_width + 1)  # Slightly larger range for clarity
plt.ylim(-1, plane_height + 1)  # Slightly larger range for clarity
plt.grid(True)

# Add the origin at (0, 0) for reference
plt.axhline(0, color='black', linewidth=0.5)
plt.axvline(0, color='black', linewidth=0.5)

# Aspect ratio set to 'equal' to keep the proportions correct
plt.gca().set_aspect('equal', adjustable='box')

# Display the plot
plt.legend()
plt.show()

# Create a square uniform mesh with uniform mesh size
gmsh.model.geo.addPoint(0, 0, 0, lc, 101)
gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 102)
gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 103)
gmsh.model.geo.addPoint(0, plane_height, 0, lc, 104)
gmsh.model.geo.addLine(101, 102, 101)
gmsh.model.geo.addLine(103, 102, 102)
gmsh.model.geo.addLine(103, 104, 103)
gmsh.model.geo.addLine(104, 101, 104)

gmsh.model.geo.addCurveLoop([101, -102, 103, 104], 1)

# Define points for the arrow shape
gmsh.model.geo.addPoint(p1.x, p1.y, 0, cm, 1)
gmsh.model.geo.addPoint(p2.x, p2.y, 0, cm, 2)
gmsh.model.geo.addPoint(p3.x, p3.y, 0, cm, 3)
gmsh.model.geo.addPoint(p4.x, p4.y, 0, cm, 4)
gmsh.model.geo.addPoint(p5.x, p5.y, 0, cm, 5)
gmsh.model.geo.addPoint(p6.x, p6.y, 0, cm, 6)
gmsh.model.geo.addPoint(p7.x, p7.y, 0, cm, 7)
gmsh.model.geo.addPoint(p8.x, p8.y, 0, cm, 8)
gmsh.model.geo.addPoint(p9.x, p9.y, 0, cm, 9)
gmsh.model.geo.addPoint(p10.x, p10.y, 0, cm, 10)
gmsh.model.geo.addPoint(p11.x, p11.y, 0, cm, 11)
gmsh.model.geo.addPoint(p12.x, p12.y, 0, cm, 12)
gmsh.model.geo.addPoint(p13.x, p13.y, 0, cm, 13)
gmsh.model.geo.addPoint(p14.x, p14.y, 0, cm, 14)

# Define lines for the pore shape
spline1 = gmsh.model.geo.addSpline([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 1])

# Create a curve loop for the pore
gmsh.model.geo.addCurveLoop([spline1], 2)

# Initialize list to store curve loops for the main surface and pores
curve_loops = [1, 2]

# Create surface of the shape
surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)

gmsh.model.geo.synchronize()

gmsh.model.mesh.generate(2)
gmsh.write(f"Morphologies/ArrowMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()
