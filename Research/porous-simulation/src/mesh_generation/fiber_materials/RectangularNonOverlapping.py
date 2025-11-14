import random
import math
import multiprocessing
import gmsh
import FiberXdmfConverter
import os
import time
import numpy as np
from shapely.geometry import Polygon, box

class Pore:
    def __init__(self, x, y, width, height, angle):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.angle = angle
        self.polygon = self.create_polygon()

    def create_polygon(self):
        w, h = self.width / 2, self.height / 2
        corners = [
            (-w, -h), (w, -h), (w, h), (-w, h)
        ]
        rotated_corners = [self.rotate_point(x, y) for x, y in corners]
        return Polygon(rotated_corners)

    def rotate_point(self, x, y):
        cos_a = math.cos(self.angle)
        sin_a = math.sin(self.angle)
        rx = x * cos_a - y * sin_a + self.x
        ry = x * sin_a + y * cos_a + self.y
        return (rx, ry)

def generate_pores(n_pores, w_min, w_max, h_min, h_max, w_width, w_height, target_pore_area_ratio, min_dist):
    cell_size = min(w_min, h_min) / 2
    grid_width = int(w_width / cell_size)
    grid_height = int(w_height / cell_size)
    grid = np.zeros((grid_height, grid_width), dtype=bool)

    pores = []
    total_area = w_width * w_height
    current_pore_area = 0
    target_pore_area = total_area * target_pore_area_ratio

    plane = box(0, 0, w_width, w_height)

    max_attempts = 100000  # Reduced max attempts
    attempts = 0

    while len(pores) < n_pores and current_pore_area < target_pore_area and attempts < max_attempts:
        x = random.uniform(0, w_width)
        y = random.uniform(0, w_height)
        width = random.uniform(w_min, w_max)
        height = random.uniform(h_min, h_max)
        angle = random.uniform(0, math.pi)

        new_pore = Pore(x, y, width, height, angle)

        if not plane.contains(new_pore.polygon.buffer(min_dist)):
            attempts += 1
            continue

        if any(new_pore.polygon.buffer(min_dist).intersects(pore.polygon) for pore in pores):
            attempts += 1
            continue

        pores.append(new_pore)
        current_pore_area += width * height
        attempts += 1

    print(f"Generated {len(pores)} pores")
    print(f"Pore area ratio: {current_pore_area / total_area:.2f}")
    print(f"Total attempts: {attempts}")

    return pores

def generate_model(model_index, num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, lc, cm, target_pore_area_ratio, min_dist):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")
    # Generate pores for this model
    pores = generate_pores(num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, target_pore_area_ratio, min_dist)

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
    FiberXdmfConverter.convert_msh_to_xdmf(f"{output_dir}/mesh{model_index + 1}.msh",
                                      f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf")

    gmsh.finalize()

if __name__ == "__main__":
    num_pores = 20
    plane_width = 1
    plane_height = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 1
    target_pore_area_ratio = 0.3
    minDist = 0.025

    width_min = 0.02
    width_max = 0.05
    height_min = 0.10
    height_max = 0.40

    start_time = time.time()

    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model, [(i, num_pores, width_min, width_max, height_min, height_max, plane_width,
                                       plane_height, lc, cm, target_pore_area_ratio, minDist)
                                      for i in range(num_models)])

    end_time = time.time()
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")
