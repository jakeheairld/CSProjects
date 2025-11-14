# Author: Jake Heairld
# Last Updated: 1/28/25
#
# Description: This script generates a 2D mesh model with randomly distributed pores. The pores are rectangles
# with random sizes, positions, and orientations. The script uses the GMSH library to generate the
# model geometry, create a mesh, and then converts the mesh to the XDMF format. The number of pores,
# pore size range, and other parameters can be customized. The model is generated in parallel for
# multiple instances.

import random
import math
import multiprocessing
import gmsh
from scripts.mesh_generation.converters import xdmf_converter, png_converter
import os
import time
import numpy as np
from shapely.geometry import Polygon, box

# Class representing a pore, which is a rectangle with a random position, size, and orientation.
class Pore:
    def __init__(self, x, y, width, height, angle):
        self.x = x  # x-coordinate of the pore center
        self.y = y  # y-coordinate of the pore center
        self.width = width  # Width of the pore
        self.height = height  # Height of the pore
        self.angle = angle  # Rotation angle of the pore
        self.polygon = self.create_polygon()  # Generate the polygon representation of the pore

    # Create the rectangular polygon for the pore.
    def create_polygon(self):
        w, h = self.width / 2, self.height / 2
        corners = [
            (-w, -h), (w, -h), (w, h), (-w, h)  # Corners of the unrotated rectangle
        ]
        # Rotate the corners of the rectangle according to the angle and return as a polygon
        rotated_corners = [self.rotate_point(x, y) for x, y in corners]
        return Polygon(rotated_corners)

    # Rotate a point (x, y) by the specified angle.
    def rotate_point(self, x, y):
        cos_a = math.cos(self.angle)  # Cosine of the rotation angle
        sin_a = math.sin(self.angle)  # Sine of the rotation angle
        # Rotate point around the pore center and return the new coordinates
        rx = x * cos_a - y * sin_a + self.x
        ry = x * sin_a + y * cos_a + self.y
        return (rx, ry)

# Function to generate pores within a given rectangular area.
def generate_pores(n_pores, w_min, w_max, h_min, h_max, w_width, w_height, target_pore_area_ratio, min_dist):
    # Calculate the grid size for pore placement (helps in controlling distances between pores)
    cell_size = min(w_min, h_min) / 2
    grid_width = int(w_width / cell_size)
    grid_height = int(w_height / cell_size)
    grid = np.zeros((grid_height, grid_width), dtype=bool)

    pores = []  # List to store generated pores
    total_area = w_width * w_height  # Total area of the plane
    current_pore_area = 0  # Cumulative area covered by pores
    target_pore_area = total_area * target_pore_area_ratio  # Target area to be covered by pores

    plane = box(0, 0, w_width, w_height)  # Define the 2D bounding box for the plane

    max_attempts = 100000  # Maximum number of attempts to generate valid pores
    attempts = 0  # Count of attempts made

    # Keep generating pores until we reach the target number of pores or maximum attempts
    while len(pores) < n_pores and current_pore_area < target_pore_area and attempts < max_attempts:
        # Randomly select position, size, and angle for the new pore
        x = random.uniform(0, w_width)
        y = random.uniform(0, w_height)
        width = random.uniform(w_min, w_max)
        height = random.uniform(h_min, h_max)
        angle = random.uniform(0, math.pi)

        # Create a new pore object
        new_pore = Pore(x, y, width, height, angle)

        # Check if the pore is within the plane bounds and does not overlap with existing pores
        if not plane.contains(new_pore.polygon.buffer(min_dist)):
            attempts += 1  # Increase attempt counter if pore is not valid
            continue

        if any(new_pore.polygon.buffer(min_dist).intersects(pore.polygon) for pore in pores):
            attempts += 1  # Increase attempt counter if pore overlaps with an existing pore
            continue

        # Add the valid pore to the list and update the covered area
        pores.append(new_pore)
        current_pore_area += width * height
        attempts += 1

    # Print the results of the pore generation process
    print(f"Generated {len(pores)} pores")
    print(f"Pore area ratio: {current_pore_area / total_area:.2f}")
    print(f"Total attempts: {attempts}")

    return pores  # Return the list of generated pores

# Function to generate the mesh model for a given set of parameters.
def generate_model(model_index, num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, lc, cm, target_pore_area_ratio, min_dist):
    gmsh.initialize()  # Initialize GMSH
    gmsh.model.add(f"model{model_index + 1}")  # Create a new model

    # Generate pores for this model
    pores = generate_pores(num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, target_pore_area_ratio, min_dist)

    # Define the square geometry for the plane (bounding box)
    gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
    gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
    gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
    gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
    gmsh.model.geo.addLine(1, 2, 1)
    gmsh.model.geo.addLine(3, 2, 2)
    gmsh.model.geo.addLine(3, 4, 3)
    gmsh.model.geo.addLine(4, 1, 4)
    gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

    # Define pores as rectangles and create curve loops for each
    curve_loops = [1]
    for i, pore in enumerate(pores):
        points = []
        for j, corner in enumerate(pore.polygon.exterior.coords[:-1]):
            points.append(gmsh.model.geo.addPoint(corner[0], corner[1], 0, cm))

        lines = []
        for j in range(4):
            lines.append(gmsh.model.geo.addLine(points[j], points[(j+1)%4]))

        gmsh.model.geo.addCurveLoop(lines, 2 + i)
        curve_loops.append(2 + i)

    # Create the surface from the curve loops
    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
    gmsh.model.geo.synchronize()  # Synchronize GMSH geometry with the model

    # Generate the mesh
    gmsh.model.mesh.generate(2)

    # Define output directory and write the mesh to file
    output_dir = "../../datasets/meshes/msh"
    os.makedirs(output_dir, exist_ok=True)  # Ensure the directory exists
    gmsh.write(f"{output_dir}/mesh{model_index + 1}.msh")

    # Convert the generated mesh to XDMF format
    output_xdmf_dir = "../../datasets/meshes/xdmf"
    os.makedirs(output_xdmf_dir, exist_ok=True)
    xdmf_converter.convert_msh_to_xdmf(f"{output_dir}/mesh{model_index + 1}.msh",
                                       f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf")

    output_png_dir = "../../datasets/meshes/png"
    os.makedirs(output_png_dir, exist_ok=True)
    png_converter.convert_msh_to_png(
        f"{output_dir}/mesh{model_index + 1}.msh",
        f"{output_png_dir}/mesh{model_index + 1}.png"
    )

    gmsh.finalize()  # Finalize the GMSH session

# Main script execution
if __name__ == "__main__":
    num_pores = 20  # Number of pores to generate
    plane_width = 1  # Width of the plane
    plane_height = 1  # Height of the plane
    cm = 1e-2  # Characteristic mesh size
    lc = 1e-1 / 2  # Length of characteristic mesh element
    num_models = 1  # Number of models to generate
    target_pore_area_ratio = 0.3  # Desired area coverage by pores
    minDist = 0.025  # Minimum distance between pores

    # Define pore size range
    width_min = 0.02
    width_max = 0.05
    height_min = 0.10
    height_max = 0.40

    # Track the time taken to generate the models
    start_time = time.time()

    # Generate the models in parallel using multiple processes
    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model, [(i, num_pores, width_min, width_max, height_min, height_max, plane_width,
                                       plane_height, lc, cm, target_pore_area_ratio, minDist)
                                      for i in range(num_models)])

    # Print the total time taken to generate all models
    end_time = time.time()
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")