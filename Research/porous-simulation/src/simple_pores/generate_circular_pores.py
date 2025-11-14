# Author: Jake Heairld
# Last Updated: 2/24/25
#
# Description: This script generates a 2D mesh model with randomly distributed pores. The pores are circles
# with random sizes and positions. The script uses the GMSH library to generate the
# model geometry, create a mesh, and then converts the mesh to the XDMF format. The target porosity,
# pore size range, and other parameters can be customized. The model is generated in parallel for
# multiple instances. This was an early script, later version controls porosity and pore density.

from scripts.mesh_generation.converters import xdmf_converter, png_converter
import random
import math
import multiprocessing
import gmsh
import os
import time

minDist = 0.25  # Minimum distance between pores

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
def generate_pores(target_porosity, r_min, r_max, w_width, w_height):
    pores = []
    total_area = w_width * w_height  # Total area of the plane
    pore_area = 0  # Total area of pores

    while pore_area / total_area < target_porosity:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius - minDist)
        y = random.uniform(radius + minDist, w_height - radius - minDist)

        # Create a new circle
        new_pore = Pore(x, y, radius)

        # Check for overlaps with existing pores
        if not any(new_pore.overlaps(pore) for pore in pores):
            pores.append(new_pore)
            pore_area += math.pi * (new_pore.r ** 2)  # Add area of the new pore

    return pores, pore_area

# Parallel function to generate both pores and meshes
def generate_model(model_index, target_porosity, r_min, r_max, plane_width, plane_height, uniform_mesh_quality, fine_scalar):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")

    # Time pore generation
    start_pores = time.time()
    pores, pore_area = generate_pores(target_porosity, r_min, r_max, plane_width, plane_height)
    end_pores = time.time()
    pore_time = end_pores - start_pores

    # Calculate actual porosity
    total_area = plane_width * plane_height
    actual_porosity = pore_area / total_area

    # Create underlying geometry
    gmsh.model.geo.addPoint(0, 0, 0, uniform_mesh_quality, 1)
    gmsh.model.geo.addPoint(plane_width, 0, 0, uniform_mesh_quality, 2)
    gmsh.model.geo.addPoint(plane_width, plane_height, 0, uniform_mesh_quality, 3)
    gmsh.model.geo.addPoint(0, plane_height, 0, uniform_mesh_quality, 4)
    gmsh.model.geo.addLine(1, 2, 1)
    gmsh.model.geo.addLine(3, 2, 2)
    gmsh.model.geo.addLine(3, 4, 3)
    gmsh.model.geo.addLine(4, 1, 4)
    gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

    # Define pores as circles and create curve loops for each
    curve_loops = [1]
    for i, pore in enumerate(pores):
        left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, uniform_mesh_quality * fine_scalar)
        center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, uniform_mesh_quality * fine_scalar)
        right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, uniform_mesh_quality * fine_scalar)

        gmsh.model.geo.addCircleArc(left, center, right, 5 + 2 * i)
        gmsh.model.geo.addCircleArc(right, center, left, 6 + 2 * i)
        gmsh.model.geo.addCurveLoop([5 + 2 * i, 6 + 2 * i], 2 + i)
        curve_loops.append(2 + i)

    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
    gmsh.model.geo.synchronize()

    # Time meshing
    start_mesh = time.time()
    gmsh.model.mesh.generate(2)
    end_mesh = time.time()
    mesh_time = end_mesh - start_mesh

    # Write msh file
    output_dir = "../../datasets/meshes/msh"
    os.makedirs(output_dir, exist_ok=True)
    gmsh.write(f"{output_dir}/mesh{model_index + 1}.msh")

    # Time XDMF conversion
    start_xdmf = time.time()
    output_xdmf_dir = "../../datasets/meshes/xdmf"
    os.makedirs(output_xdmf_dir, exist_ok=True)
    xdmf_converter.convert_msh_to_xdmf(
        f"{output_dir}/mesh{model_index + 1}.msh",
        f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf"
    )
    end_xdmf = time.time()
    xdmf_time = end_xdmf - start_xdmf

    # Time PNG conversion
    start_png = time.time()
    output_png_dir = "../../datasets/meshes/png"
    os.makedirs(output_png_dir, exist_ok=True)
    png_converter.convert_msh_to_png(
        f"{output_dir}/mesh{model_index + 1}.msh",
        f"{output_png_dir}/mesh{model_index + 1}.png"
    )
    end_png = time.time()
    png_time = end_png - start_png

    # Print results with timing
    print(f"Model {model_index + 1}:")
    print(f"Number of pores: {len(pores)}")
    print(f"Actual porosity: {actual_porosity * 100:.2f}%")
    print(f"Pore generation time: {pore_time:.2f} seconds")
    print(f"Meshing time: {mesh_time:.2f} seconds")
    print(f"XDMF conversion time: {xdmf_time:.2f} seconds")
    print(f"PNG conversion time: {png_time:.2f} seconds\n")

    gmsh.finalize()

# Main function for parallel execution
if __name__ == "__main__":
    # Parameters
    target_porosity = 0.3  # Target porosity (30%)
    plane_width = 10
    plane_height = 10
    fine_scalar = 0.2
    uniform_mesh_quality = 0.5
    num_models = 1
    # num_pores = 5

    # Circle pores
    radius_min = 1
    radius_max = 2

    # Start timing
    start_time = time.time()

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model, [(i, target_porosity, radius_min, radius_max, plane_width, plane_height, uniform_mesh_quality, fine_scalar)
                                      for i in range(num_models)])

    # End total timing
    end_time = time.time()

    # Print total time (wall clock)
    print(f"\nTotal wall-clock time for all models: {end_time - start_time:.2f} seconds")