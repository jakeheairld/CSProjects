import gmsh
import XdmfConverter
import PoreGenerator
import time

# Parameters
num_pores = 6
pore_type = "circle"
plane_width = 1
plane_height = 1
cm = 1e-2
lc = 1e-1/2
num_models = 50

# Circle pores
radius_min = 0.10
radius_max = 0.15

# Square_pores
side_length_min = 0.20
side_length_max = 0.30

# Ellipse pores
a_min = 0.10
a_max = 0.15
b_min = 0.05
b_max = 0.10

start_time = time.time()

if pore_type == "circle":
    models = [PoreGenerator.generate_pores(num_pores, radius_min, radius_max, plane_width, plane_height)
              for _ in range(num_models)]
elif pore_type == "square":
    models = [PoreGenerator.generate_pores_square(num_pores, side_length_min, side_length_max, plane_width, plane_height)
              for _ in range(num_models)]
elif pore_type == "ellipse":
    models = [PoreGenerator.generate_pores_ellipse(num_pores, a_min, a_max, b_min, b_max, plane_width, plane_height)
              for _ in range(num_models)]

# Initialize gmsh
gmsh.initialize()

for m in range(num_models):
    gmsh.model.add("model" + str(m+1))
    pores = models[m]

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

    if pore_type == "circle":
        # Define pores as circles and create curve loops for each
        for i in range(len(pores)):
            pore = pores[i]

            left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
            center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
            right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

            gmsh.model.geo.addCircleArc(left, center, right, 5+2*i)
            gmsh.model.geo.addCircleArc(right, center, left, 6+2*i)
            gmsh.model.geo.addCurveLoop([5+2*i, 6+2*i], 2+i)

            # Append each pore curve loop to the curve_loops list
            curve_loops.append(2+i)
    elif pore_type == "square":
        # Define pores as squares and create curve loops for each
        for i in range(len(pores)):
            pore = pores[i]
            half_side = pore.l / 2

            # Define the four corners of the square
            p1 = gmsh.model.geo.addPoint(pore.x - half_side, pore.y - half_side, 0, cm)
            p2 = gmsh.model.geo.addPoint(pore.x + half_side, pore.y - half_side, 0, cm)
            p3 = gmsh.model.geo.addPoint(pore.x + half_side, pore.y + half_side, 0, cm)
            p4 = gmsh.model.geo.addPoint(pore.x - half_side, pore.y + half_side, 0, cm)

            # Define the lines for the square
            l1 = gmsh.model.geo.addLine(p1, p2)
            l2 = gmsh.model.geo.addLine(p2, p3)
            l3 = gmsh.model.geo.addLine(p3, p4)
            l4 = gmsh.model.geo.addLine(p4, p1)

            # Create a curve loop for the square
            gmsh.model.geo.addCurveLoop([l1, l2, l3, l4], 2 + i)

            # Append each pore curve loop to the curve_loops list
            curve_loops.append(2 + i)

    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
    gmsh.model.geo.synchronize()

    gmsh.model.mesh.generate(2)
    gmsh.write(f"../SavedMeshFiles/mesh{m + 1}.msh")
    XdmfConverter.convert_msh_to_xdmf(f"../SavedMeshFiles/mesh{m + 1}.msh", f"../SavedXdmfFiles/mesh{m + 1}.xdmf")

    # Clear the current model to free up memory for the next one
    gmsh.clear()

gmsh.finalize()

# Record the end time
end_time = time.time()

# Calculate the elapsed time and print it
elapsed_time = end_time - start_time
print(f"Total time to generate all meshes: {elapsed_time:.2f} seconds")