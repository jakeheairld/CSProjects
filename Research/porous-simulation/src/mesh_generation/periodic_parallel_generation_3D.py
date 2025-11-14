import gmsh
import random
import os
import time
import multiprocessing
import XdmfConverter
import math


def generate_model(model_index, num_pores, radius_min, radius_max, cube_width, cube_height, cube_depth, lc, cm):
    gmsh.initialize()
    gmsh.model.add(f"model_{model_index + 1}")

    # Create a cube (the solid object)
    cube_tag = gmsh.model.occ.addBox(0, 0, 0, cube_width, cube_height, cube_depth, 1)
    gmsh.model.occ.synchronize()

    # Apply mesh size constraints
    gmsh.model.mesh.setSize(gmsh.model.getEntities(0), lc)
    gmsh.model.mesh.setSize([(0, 1)], cm)

    # Initialize lists for storing pore positions and radii
    pore_radius = []
    pore_positions = []

    # Ensure no overlapping spheres
    for _ in range(num_pores):
        overlap = True
        while overlap:
            # Randomly generate radius and position for the new pore
            radius = random.uniform(radius_min, radius_max)
            position = (
                random.uniform(0, cube_width),
                random.uniform(0, cube_height),
                random.uniform(0, cube_depth)
            )

            # Check if the new sphere overlaps with any existing spheres
            overlap = False
            for i, (x, y, z) in enumerate(pore_positions):
                distance = math.sqrt((position[0] - x) ** 2 + (position[1] - y) ** 2 + (position[2] - z) ** 2)
                if distance < (
                        radius + pore_radius[i]):  # If the distance is less than the sum of radii, it's an overlap
                    overlap = True
                    break

        # Add the valid pore to the lists
        pore_radius.append(radius)
        pore_positions.append(position)

    # Add spherical pores to subtract from the cube
    pore_tags = []
    for i, (x, y, z) in enumerate(pore_positions):
        pore_tag = gmsh.model.occ.addSphere(x, y, z, pore_radius[i], i + 2)
        pore_tags.append(pore_tag)

    # Subtract the spheres from the cube to create pores (holes)
    try:
        # The cube (tagged as 1) and the pores (tagged as 2, 3, etc.)
        # Perform the subtraction (hole creation)
        gmsh.model.occ.cut([(3, cube_tag)], [(3, i) for i in range(2, num_pores + 2)])
        gmsh.model.occ.synchronize()
    except Exception as e:
        print(f"Error during subtraction: {str(e)}")

    # Get surfaces created in the model
    surfaces = gmsh.model.getEntities(2)  # 2 refers to surfaces, adjust if needed
    print(f"Surfaces for model {model_index + 1}: {surfaces}")

    # Debugging - Ensure you are working with the correct surface tags
    if len(surfaces) < 2:
        print(f"Warning: Not enough surfaces found for model {model_index + 1}. Available surfaces: {surfaces}")

    # Define periodic boundaries (if necessary, based on your geometry)
    try:
        gmsh.model.mesh.setPeriodic(2, [2], [1],
                                    [1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1])  # x-direction periodicity
    except Exception as e:
        print(f"Error applying periodicity between surfaces 1 and 2 for model {model_index + 1}: {str(e)}")

    # Additional periodicity can be set here if needed (y and z directions)

    # Generate mesh
    try:
        gmsh.model.mesh.generate(3)
    except Exception as e:
        print(f"Error during mesh generation for model {model_index + 1}: {str(e)}")
        gmsh.finalize()
        return

    # Output mesh file
    output_dir = "../SavedMeshFiles"
    os.makedirs(output_dir, exist_ok=True)
    gmsh.write(f"{output_dir}/mesh{model_index + 1}.msh")

    # Convert to XDMF format
    output_xdmf_dir = "../SavedXdmfFiles"
    os.makedirs(output_xdmf_dir, exist_ok=True)
    try:
        XdmfConverter.convert_msh_to_xdmf(f"{output_dir}/mesh{model_index + 1}.msh",
                                          f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf")
    except Exception as e:
        print(f"Error during XDMF conversion: {str(e)}")

    gmsh.finalize()


# Main function for parallel execution
if __name__ == "__main__":
    # Parameters
    num_pores = 300
    cube_width = 1
    cube_height = 1
    cube_depth = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 1

    # Spherical pores
    radius_min = 0.05
    radius_max = 0.1

    # Start timing
    start_time = time.time()

    # Create a process pool for parallel execution
    try:
        with multiprocessing.Pool() as pool:
            pool.starmap(generate_model,
                         [(i, num_pores, radius_min, radius_max, cube_width, cube_height, cube_depth, lc, cm)
                          for i in range(num_models)])
    except Exception as e:
        print(f"Error during parallel execution: {str(e)}")

    # End timing
    end_time = time.time()

    # Print total time taken
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")
