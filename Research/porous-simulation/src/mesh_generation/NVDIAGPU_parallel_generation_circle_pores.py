import random
import math
import concurrent.futures
import gmsh
import XdmfConverter
import os
import time
import cupy as cp
import numpy as np

minDist = 0.025  # Minimum distance between pores


class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r
        self.type = 'Circle'


def generate_pores_gpu(n_pores, r_min, r_max, w_width, w_height):
    # Generate more candidates than needed
    n_candidates = n_pores * 2

    # Generate random positions and radii on GPU
    radii = cp.random.uniform(r_min, r_max, n_candidates)
    x = cp.random.uniform(radii + minDist, w_width - radii - minDist, n_candidates)
    y = cp.random.uniform(radii + minDist, w_height - radii - minDist, n_candidates)

    # Combine into a single array for easier processing
    pores = cp.column_stack((x, y, radii))

    # Check for overlaps
    valid_pores = []
    for i in range(n_candidates):
        if len(valid_pores) >= n_pores:
            break

        current_pore = pores[i]
        distances = cp.sqrt(cp.sum((pores[:i] - current_pore[:2]) ** 2, axis=1))
        if cp.all(distances >= (current_pore[2] + pores[:i, 2] + minDist)):
            valid_pores.append(Pore(*current_pore.get()))

    return valid_pores


def generate_model(model_index, num_pores, r_min, r_max, plane_width, plane_height, lc, cm):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")

    # Generate pores using GPU
    pores = generate_pores_gpu(num_pores, r_min, r_max, plane_width, plane_height)

    # Create square geometry
    points = [
        gmsh.model.geo.addPoint(0, 0, 0, lc),
        gmsh.model.geo.addPoint(plane_width, 0, 0, lc),
        gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc),
        gmsh.model.geo.addPoint(0, plane_height, 0, lc)
    ]
    lines = [gmsh.model.geo.addLine(points[i], points[(i + 1) % 4]) for i in range(4)]
    gmsh.model.geo.addCurveLoop(lines, 1)

    # Define pores as circles and create curve loops
    curve_loops = [1]
    for i, pore in enumerate(pores):
        center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
        left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
        right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

        arcs = [
            gmsh.model.geo.addCircleArc(left, center, right),
            gmsh.model.geo.addCircleArc(right, center, left)
        ]
        curve_loops.append(gmsh.model.geo.addCurveLoop(arcs))

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


if __name__ == "__main__":
    # Parameters
    num_pores = 9
    plane_width = 1
    plane_height = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 500

    # Circle pores
    radius_min = 0.10
    radius_max = 0.15

    start_time = time.time()

    # Use ThreadPoolExecutor for parallel execution
    with concurrent.futures.ThreadPoolExecutor() as executor:
        futures = [
            executor.submit(generate_model, i, num_pores, radius_min, radius_max, plane_width, plane_height, lc, cm)
            for i in range(num_models)]
        concurrent.futures.wait(futures)

    end_time = time.time()
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")
