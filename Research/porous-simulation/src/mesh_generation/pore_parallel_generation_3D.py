import random
import math
import multiprocessing
import gmsh
import XdmfConverter
import os
import time

minDist = 0.025  # Minimum distance between pores

# Sphere pore class
class Pore:
    def __init__(self, x, y, z, r):
        self.x = x
        self.y = y
        self.z = z
        self.r = r
        self.type = 'Sphere'

    # Method to check if two spheres overlap
    def overlaps(self, other_pore):
        distance = math.sqrt((self.x - other_pore.x) ** 2 +
                             (self.y - other_pore.y) ** 2 +
                             (self.z - other_pore.z) ** 2)
        return distance < (self.r + other_pore.r + minDist)

# Function to generate pores for a single model
def generate_pores(n_pores, r_min, r_max, w_width, w_height, w_depth):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius - minDist)
        y = random.uniform(radius + minDist, w_height - radius - minDist)
        z = random.uniform(radius + minDist, w_depth - radius - minDist)

        # Create a new sphere
        new_pore = Pore(x, y, z, radius)

        # Check for overlaps with existing pores
        if not any(new_pore.overlaps(pore) for pore in pores):
            pores.append(new_pore)

    return pores

# Parallel function to generate both pores and meshes
def generate_model(model_index, num_pores, r_min, r_max, cube_width, cube_height, cube_depth, lc, cm):
    gmsh.initialize()
    gmsh.model.add(f"model{model_index + 1}")
    # Generate pores for this model
    pores = generate_pores(num_pores, r_min, r_max, cube_width, cube_height, cube_depth)

    # Create cube geometry
    gmsh.model.occ.addPoint(0, 0, 0, lc, 1)
    gmsh.model.occ.addPoint(cube_width, 0, 0, lc, 2)
    gmsh.model.occ.addPoint(cube_width, cube_height, 0, lc, 3)
    gmsh.model.occ.addPoint(0, cube_height, 0, lc, 4)
    gmsh.model.occ.addPoint(0, 0, cube_depth, lc, 5)
    gmsh.model.occ.addPoint(cube_width, 0, cube_depth, lc, 6)
    gmsh.model.occ.addPoint(cube_width, cube_height, cube_depth, lc, 7)
    gmsh.model.occ.addPoint(0, cube_height, cube_depth, lc, 8)
    box = gmsh.model.occ.addBox(0, 0, 0, cube_width, cube_height, cube_depth)

    # Define pores as spheres
    for i, pore in enumerate(pores):
        gmsh.model.occ.addSphere(pore.x, pore.y, pore.z, pore.r, tag=10 + i)

    gmsh.model.occ.synchronize()

    # Define periodic boundaries
    gmsh.model.occ.addPeriodic(
        2,  # Dimension (faces)
        [4, 2],  # Slave face tags
        [3, 1],  # Master face
    )
    # Define periodic boundaries
    # Periodic in x-direction
    gmsh.model.occ.addPeriodic(
        1,  # Dimension (edges)
        [1, 2],  # Slave edge tags (x-direction)
        [5, 6],  # Master edge tags (opposite faces in x-direction)
    )
    # Periodic in y-direction
    gmsh.model.occ.addPeriodic(
        1,  # Dimension (edges)
        [2, 3],  # Slave edge tags (y-direction)
        [6, 7],  # Master edge tags (opposite faces in y-direction)
    )
    # Periodic in z-direction
    gmsh.model.occ.addPeriodic(
        1,  # Dimension (edges)
        [3, 4],  # Slave edge tags (z-direction)
        [7, 8],  # Master edge tags (opposite faces in z-direction)
    )

    # Generate mesh
    gmsh.model.mesh.generate(3)

    # Output mesh file
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
    num_pores = 6
    cube_width = 1
    cube_height = 1
    cube_depth = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 5

    # Spherical pores
    radius_min = 0.10
    radius_max = 0.15

    # Start timing
    start_time = time.time()

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.starmap(generate_model,
                     [(i, num_pores, radius_min, radius_max, cube_width, cube_height, cube_depth, lc, cm)
                      for i in range(num_models)])

    # End timing
    end_time = time.time()

    # Print total time taken
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")