import random
import math
import multiprocessing
import gmsh
import XdmfConverter
import os
import time
import cProfile

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
        return distance < (self.r + other_pore.r + 0.025)

# Function to generate pores for a single model
def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + 0.025, w_width - radius - 0.025)
        y = random.uniform(radius + 0.025, w_height - radius - 0.025)

        # Create a new circle
        new_pore = Pore(x, y, radius)

        # Check for overlaps with existing pores
        if not any(new_pore.overlaps(pore) for pore in pores):
            pores.append(new_pore)

    return pores

# Function to generate both pores and meshes for a batch of models
def generate_models_batch(args):
    batch, batch_size, num_pores, r_min, r_max, plane_width, plane_height, lc, cm = args
    for model_index in batch:
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

def main():
    num_pores = 6
    plane_width = 1
    plane_height = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 1000
    batch_size = 10  # Adjust batch size for parallel processing

    # Circle pores
    radius_min = 0.10
    radius_max = 0.15

    # Split models into batches for parallel processing
    batches = [list(range(i, min(i + batch_size, num_models))) for i in range(0, num_models, batch_size)]

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.map(generate_models_batch,
                 [(batch, batch_size, num_pores, radius_min, radius_max, plane_width, plane_height, lc, cm) for batch in
                  batches])

if __name__ == "__main__":
    cProfile.run('main()')  # This will run the `main()` function and profile it
