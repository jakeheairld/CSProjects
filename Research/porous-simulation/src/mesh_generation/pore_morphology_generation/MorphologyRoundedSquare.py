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

p1 = Point(2, 0)
p2 = Point(8, 0)
p3 = Point(9.5, 0.5)
p4 = Point(10, 2)
p5 = Point(10, 8)
p6 = Point(9.5, 9.5)
p7 = Point(8, 10)
p8 = Point(2, 10)
p9 = Point(0.5, 9.5)
p10 = Point(0, 8)
p11 = Point(0, 2)
p12 = Point(0.5, 0.5)
points = [p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12]

# Define points for the octagon shape
gmsh.model.geo.addPoint(p1.x, p1.y, 0, lc, 1)
gmsh.model.geo.addPoint(p2.x, p2.y, 0, lc, 2)
gmsh.model.geo.addPoint(p3.x, p3.y, 0, lc, 3)
gmsh.model.geo.addPoint(p4.x, p4.y, 0, lc, 4)
gmsh.model.geo.addPoint(p5.x, p5.y, 0, lc, 5)
gmsh.model.geo.addPoint(p6.x, p6.y, 0, lc, 6)
gmsh.model.geo.addPoint(p7.x, p7.y, 0, lc, 7)
gmsh.model.geo.addPoint(p8.x, p8.y, 0, lc, 8)
gmsh.model.geo.addPoint(p9.x, p9.y, 0, lc, 9)
gmsh.model.geo.addPoint(p10.x, p10.y, 0, lc, 10)
gmsh.model.geo.addPoint(p11.x, p11.y, 0, lc, 11)
gmsh.model.geo.addPoint(p12.x, p12.y, 0, lc, 12)


# Collect coordinates for points p1 to p12
point_coords = []

# Get coordinates for each point using gmsh.model.geo.getPoint
for point in points:
    point_coords.append((point.x, point.y))

# Plot the points p1 to p12 using matplotlib
x_coords, y_coords = zip(*point_coords)
plt.figure(figsize=(8, 8))
plt.scatter(x_coords, y_coords, color='red', label='Points p1 to p12')
plt.title('Rounded Square Shape (Points p1 to p12)')
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

# Define lines for the octagon shape
l1 = gmsh.model.geo.addLine(1, 2)
spline1 = gmsh.model.geo.addSpline([2, 3, 4])  # Corner from p2 -> p3 -> p4
l4 = gmsh.model.geo.addLine(4, 5)
spline2 = gmsh.model.geo.addSpline([5, 6, 7])  # Corner from p5 -> p6 -> p7
l7 = gmsh.model.geo.addLine(7, 8)
spline3 = gmsh.model.geo.addSpline([8, 9, 10])  # Corner from p8 -> p9 -> p10
l10 = gmsh.model.geo.addLine(10, 11)
spline4 = gmsh.model.geo.addSpline([11, 12, 1])  # Corner from p11 -> p12 -> p1

# Create a curve loop for the octagon
gmsh.model.geo.addCurveLoop([l1, spline1, l4, spline2, l7, spline3, l10, spline4], 1)

# Initialize list to store curve loops for the main surface and pores
curve_loops = [1]

# Create the surface for the octagon
surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)

gmsh.model.geo.synchronize()

gmsh.model.mesh.generate(2)

# Print the model name and dimension:
print('This model is of dimension:' + ' (' +
      str(gmsh.model.getDimension()) + 'D)')

gmsh.write(f"Morphologies/RoundedSquareMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()
