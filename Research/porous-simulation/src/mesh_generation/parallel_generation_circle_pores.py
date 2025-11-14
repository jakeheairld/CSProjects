import random
import math
import multiprocessing
import gmsh
import XdmfConverter
import os
import time

minDist = 0.025  # Minimum distance between pores

# Circle pore class
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r
        self.type = 'Circle'

    # Method to check if two circles overlap
    def overlaps(self, other_pore):
        distance = math.sqrt((self.x - other_pore.x) ** 2 + (self.y - other_pore.y) ** 2)
        return distance < (self.r + other_pore.r + minDist)

# Function to generate pores for a single model
def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius - minDist)
        y = random.uniform(radius + minDist, w_height - radius - minDist)

        # Create a new circle
        new_pore = Pore(x, y, radius)

        # Check for overlaps with existing pores
        if not any(new_pore.overlaps(pore) for pore in pores):
            pores.append(new_pore)

    return pores

# Parallel function to generate both pores and meshes
def generate_model(model_index, num_pores, r_min, r_max, plane_width, plane_height, lc, cm):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")
    # Generate pores for this model
    pores = generate_pores(num_pores, r_min, r_max, plane_width, plane_height)

    # Create square geometry
    gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
    gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
    gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
    gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
    gmsh.model.geo.addLine(1, 2, 1)
    gmsh.model.geo.addLine(3, 2, 2)
    gmsh.model.geo.addLine(3, 4, 3)
    gmsh.model.geo.addLine(4, 1, 4)
    gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

    # Define pores as circles and create curve loops for each
    curve_loops = [1]
    for i, pore in enumerate(pores):
        left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
        center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
        right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

        gmsh.model.geo.addCircleArc(left, center, right, 5 + 2 * i)
        gmsh.model.geo.addCircleArc(right, center, left, 6 + 2 * i)
        gmsh.model.geo.addCurveLoop([5 + 2 * i, 6 + 2 * i], 2 + i)
        curve_loops.append(2 + i)

    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
    gmsh.model.geo.synchronize()

    # Generate mesh
    gmsh.model.mesh.generate(2)
    output_dir = "../SavedMeshFiles"
    os.makedirs(output_dir, exist_ok=True)
    gmsh.write(f"{output_dir}/mesh{model_index + 1}.msh")

    # Convert to XDMF format
    output_xdmf_dir = "../SavedXdmfFiles"
    os.makedirs(output_xdmf_dir, exist_ok=True)
    XdmfConverter.convert_msh_to_xdmf(f"{output_dir}/mesh{model_index + 1}.msh",
                                      f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf")

    gmsh.finalize()


# Main function for parallel execution
if __name__ == "__main__":
    # Parameters
    num_pores = 200
    plane_width = 10
    plane_height = 10
    cm = (0.1)
    lc = (0.05)
    num_models = 5

    # Circle pores
    radius_min = 0.05
    radius_max = 0.45

    # Start timing
    start_time = time.time()

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model, [(i, num_pores, radius_min, radius_max, plane_width, plane_height, lc, cm)
                                      for i in range(num_models)])

    # End timing
    end_time = time.time()

    # Print total time taken
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")