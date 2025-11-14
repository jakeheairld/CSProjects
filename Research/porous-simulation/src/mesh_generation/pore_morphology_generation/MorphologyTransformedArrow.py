import gmsh
import matplotlib.pyplot as plt
import numpy as np

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

# Define points for the original shape
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

# Function to calculate the center of the shape
def calculate_center(points):
    x_coords, y_coords = zip(*[(point.x, point.y) for point in points])
    center_x = sum(x_coords) / len(points)
    center_y = sum(y_coords) / len(points)
    return (center_x, center_y)

# Center of the arrow shape
center_x, center_y = calculate_center(points)

# Function to apply a transformation (rotation and scaling) around the center
def transform_point(point, center_x, center_y, scale, angle):
    # Translate point to origin
    translated_x = point.x - center_x
    translated_y = point.y - center_y

    # Apply rotation
    rotated_x = translated_x * np.cos(angle) - translated_y * np.sin(angle)
    rotated_y = translated_x * np.sin(angle) + translated_y * np.cos(angle)

    # Apply scaling
    scaled_x = rotated_x * scale
    scaled_y = rotated_y * scale

    # Translate point back
    new_x = scaled_x + center_x
    new_y = scaled_y + center_y

    return Point(new_x, new_y)

# Parameters for the transformation
scale_factor = 0.7  # Adjust Size
rotation_angle = np.radians(190)  # Adjust Rotation

# Apply transformation to all points
transformed_points = [transform_point(point, center_x, center_y, scale_factor, rotation_angle) for point in points]

# Collect coordinates for transformed points
transformed_coords = [(point.x, point.y) for point in transformed_points]

# Plot the transformed points using matplotlib
x_coords, y_coords = zip(*transformed_coords)
plt.figure(figsize=(8, 8))
plt.scatter(x_coords, y_coords, color='blue', label='Transformed Arrow Shape')
plt.title('Transformed Arrow Shape (Rotated and Scaled)')
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

# Initialize gmsh model
gmsh.model.geo.addPoint(0, 0, 0, lc, 101)
gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 102)
gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 103)
gmsh.model.geo.addPoint(0, plane_height, 0, lc, 104)
gmsh.model.geo.addLine(101, 102, 101)
gmsh.model.geo.addLine(103, 102, 102)
gmsh.model.geo.addLine(103, 104, 103)
gmsh.model.geo.addLine(104, 101, 104)

gmsh.model.geo.addCurveLoop([101, -102, 103, 104], 1)

# Define points for the transformed arrow shape
for i, point in enumerate(transformed_points, 1):
    gmsh.model.geo.addPoint(point.x, point.y, 0, cm, i)

ids_for_spline = [i for i in range(1, len(transformed_points)+1)]
ids_for_spline.append(1)
# Define lines for the transformed arrow shape
spline1 = gmsh.model.geo.addSpline(ids_for_spline)

# Create a curve loop for the transformed pore
gmsh.model.geo.addCurveLoop([spline1], 2)

# Initialize list to store curve loops for the main surface and pores
curve_loops = [1, 2]

# Create surface of the transformed shape
surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)

gmsh.model.geo.synchronize()

# Generate mesh and write to file
gmsh.model.mesh.generate(2)
gmsh.write(f"Morphologies/TransformedArrowMorphology.msh")

# Clear the current model to free up memory for the next one
gmsh.clear()

gmsh.finalize()
