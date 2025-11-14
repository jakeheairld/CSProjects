import gmsh

def create_arrow_pore(x_center, y_center, size):
    # Parameters for the arrow geometry
    tip_height = size * 1.1  # Tip height
    base_width = size
    bottom_depth = size * 0.3  # Bottom concave depth
    side_bulge = size * 0.005  # Reduced bulge on the sides

    # Define key points
    p1 = gmsh.model.geo.addPoint(x_center - base_width / 2, y_center, 0, cm)  # Left base
    p3 = gmsh.model.geo.addPoint(x_center + base_width / 2, y_center, 0, cm)  # Right base
    p6 = gmsh.model.geo.addPoint(x_center, y_center + bottom_depth, 0, cm)   # Bottom concave

    # Define arc center for the tip
    tip_radius = size * 0.1  # Smaller radius for a sharper tip
    tip_center_x = x_center
    tip_center_y = y_center + tip_height - tip_radius

    # Add the center of the tip arc
    tip_center = gmsh.model.geo.addPoint(tip_center_x, tip_center_y, 0, cm)

    # Define key points for the tip arc
    tip_left = gmsh.model.geo.addPoint(x_center - base_width / 8, y_center + tip_height, 0, cm)
    tip_right = gmsh.model.geo.addPoint(x_center + base_width / 8, y_center + tip_height, 0, cm)

    # Add slightly inward bulging points
    bulge_left = gmsh.model.geo.addPoint(x_center - base_width / 2 - side_bulge, y_center + tip_height / 2, 0, cm)
    bulge_right = gmsh.model.geo.addPoint(x_center + base_width / 2 + side_bulge, y_center + tip_height / 2, 0, cm)

    # Left curve (p1 -> bulge_left -> tip_left)
    left_curve = gmsh.model.geo.addSpline([p1, bulge_left, tip_left])

    # Tip arc (tip_left -> tip_right)
    tip_curve = gmsh.model.geo.addCircleArc(tip_left, tip_center, tip_right)

    # Right curve (tip_right -> bulge_right -> p3)
    right_curve = gmsh.model.geo.addSpline([tip_right, bulge_right, p3])

    # Bottom curve (p3 -> p6 -> p1)
    bottom_curve = gmsh.model.geo.addSpline([p3, p6, p1])

    # Create a closed curve loop
    return gmsh.model.geo.addCurveLoop([left_curve, tip_curve, right_curve, bottom_curve])

# Initialize Gmsh
gmsh.initialize()

# Domain parameters
plane_width = 10
plane_height = 10
cm = 0.1  # Mesh size for the pore boundary
lc = 0.5  # Mesh size for the domain

# Define the domain
gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
gmsh.model.geo.addLine(1, 2, 1)
gmsh.model.geo.addLine(2, 3, 2)
gmsh.model.geo.addLine(3, 4, 3)
gmsh.model.geo.addLine(4, 1, 4)
gmsh.model.geo.addCurveLoop([1, 2, 3, 4], 1)

# Create the arrow pore
arrow_curve_loop = create_arrow_pore(5.0, 5.0, 2.0)

# Add the arrow pore curve loop to the surface
surface_loops = [1, arrow_curve_loop]
surface_tag = gmsh.model.geo.addPlaneSurface(surface_loops)

# Synchronize and generate the mesh
gmsh.model.geo.synchronize()
gmsh.model.mesh.generate(2)

# Write the mesh to file
output_path = "Morphologies/OldArrowMorphology.msh"
gmsh.write(output_path)
print(f"Arrow pore mesh saved to {output_path}")

# Finalize Gmsh
gmsh.finalize()