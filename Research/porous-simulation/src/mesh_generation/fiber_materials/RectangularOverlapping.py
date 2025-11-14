import random
import math
import multiprocessing
import gmsh
import FiberXdmfConverter
import os
import time
import numpy as np

minDist = 0.025  # Minimum distance between pores

class Pore:
    def __init__(self, x, y, width, height, angle):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.angle = angle
        self.type = 'Rectangle'

def generate_pores(n_pores, w_min, w_max, h_min, h_max, w_width, w_height, target_pore_area_ratio):
    pores = []
    total_area = w_width * w_height
    current_pore_area = 0
    target_pore_area = total_area * target_pore_area_ratio

    while len(pores) < n_pores and current_pore_area < target_pore_area:
        width = random.uniform(w_min, w_max)
        height = random.uniform(h_min, h_max)
        angle = random.uniform(0, math.pi)
        x = random.uniform(0, w_width)
        y = random.uniform(0, w_height)

        new_pore = Pore(x, y, width, height, angle)
        pores.append(new_pore)
        current_pore_area += width * height

    print(f"Generated {len(pores)} pores")
    print(f"Pore area ratio: {current_pore_area / total_area:.2f}")

    return pores


def generate_model(model_index, num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, lc, cm,
                   target_pore_area_ratio):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")

    # Create main rectangle
    main_rect = gmsh.model.occ.addRectangle(0, 0, 0, plane_width, plane_height)

    pores = generate_pores(num_pores, w_min, w_max, h_min, h_max, plane_width, plane_height, target_pore_area_ratio)

    pore_entities = []
    pore_lines = []
    for pore in pores:
        # Create a rectangle for each pore
        pore_rect = gmsh.model.occ.addRectangle(pore.x - pore.width / 2, pore.y - pore.height / 2, 0, pore.width,
                                                pore.height)
        # Rotate the pore
        gmsh.model.occ.rotate([(2, pore_rect)], pore.x, pore.y, 0, 0, 0, 1, pore.angle)
        pore_entities.append((2, pore_rect))

        # Synchronize to ensure the geometry is updated
        gmsh.model.occ.synchronize()

        # Get the boundary lines of the pore using gmsh.model.getBoundary()
        lines = gmsh.model.getBoundary([(2, pore_rect)])
        pore_lines.extend([abs(line[1]) for line in lines])

    # Boolean difference to create pores
    gmsh.model.occ.cut([(2, main_rect)], pore_entities)

    gmsh.model.occ.synchronize()

    # Create a distance field
    gmsh.model.mesh.field.add("Distance", 1)
    gmsh.model.mesh.field.setNumbers(1, "CurvesList", pore_lines)
    gmsh.model.mesh.field.setNumber(1, "Sampling", 100)

    # Create a threshold field
    gmsh.model.mesh.field.add("Threshold", 2)
    gmsh.model.mesh.field.setNumber(2, "InField", 1)
    gmsh.model.mesh.field.setNumber(2, "SizeMin", cm / 2)
    gmsh.model.mesh.field.setNumber(2, "SizeMax", lc)
    gmsh.model.mesh.field.setNumber(2, "DistMin", 0.5 * minDist)
    gmsh.model.mesh.field.setNumber(2, "DistMax", 2 * minDist)

    # Set the threshold field as background mesh
    gmsh.model.mesh.field.setAsBackgroundMesh(2)

    # Generate mesh
    gmsh.option.setNumber("Mesh.Algorithm", 6)  # Frontal-Delaunay for 2D meshes
    gmsh.option.setNumber("Mesh.RecombineAll", 1)  # Recombine all triangular meshes into quads
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
    # Parameters
    num_pores = 25
    plane_width = 1
    plane_height = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 1
    target_pore_area_ratio = 0.3  # Adjust this value to control pore volume

    # Rectangle pores
    width_min = 0.02
    width_max = 0.05
    height_min = 0.10
    height_max = 0.40

    start_time = time.time()

    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model, [(i, num_pores, width_min, width_max, height_min, height_max, plane_width,
                                       plane_height, lc, cm, target_pore_area_ratio)
                                      for i in range(num_models)])

    end_time = time.time()
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")
