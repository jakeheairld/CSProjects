import time
import gmsh
import XdmfConverter
import PoreGenerator
from concurrent.futures import ProcessPoolExecutor

# Works on linux systems, not yet compatible with Windows

# Start the timer
start_time = time.time()

# Parameters
pore_type = "circle"
num_pores = 7
radius_min = 0.05
radius_max = 0.15
plane_width = 1
plane_height = 1
cm = 1e-2
lc = 1e-1/2
num_models = 3
models = [PoreGenerator.generate_pores(num_pores, radius_min, radius_max, plane_width, plane_height)
          for _ in range(num_models)]

# Generate mesh for each model in a batch
def generate_mesh_batch(model_ids, batch_models):
    gmsh.initialize()
    try:
        for model_id, pores in zip(model_ids, batch_models):
            gmsh.model.add(f"model{model_id + 1}")

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

            # Define pores as circles and create curve loops for each
            for i, pore in enumerate(pores):
                left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
                center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
                right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

                gmsh.model.geo.addCircleArc(left, center, right, 5 + 2 * i)
                gmsh.model.geo.addCircleArc(right, center, left, 6 + 2 * i)
                gmsh.model.geo.addCurveLoop([5 + 2 * i, 6 + 2 * i], 2 + i)

                # Append each pore curve loop to the curve_loops list
                curve_loops.append(2 + i)

            surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
            gmsh.model.geo.synchronize()

            # Generates 2-Dimensional Mesh
            gmsh.model.mesh.generate(2)

            gmsh.write(f"../SavedMeshFiles/mesh{model_id + 1}.msh")
            XdmfConverter.convert_msh_to_xdmf(f"../SavedMeshFiles/mesh{model_id + 1}.msh",f"../SavedXdmfFiles/mesh{model_id + 1}.xdmf")
            gmsh.model.remove()  # Clear model after each mesh

    except Exception as e:
        print(f"Error with model {model_id + 1}: {e}")

    finally:
        gmsh.finalize()

# Split models into batches for processing
batch_size = num_models // 2  # Adjust batch size based on performance tests
batches = [models[i:i + batch_size] for i in range(0, num_models, batch_size)]
batch_ids = [list(range(i, i + batch_size)) for i in range(0, num_models, batch_size)]

# Run mesh generation in parallel
with ProcessPoolExecutor(max_workers=2) as executor:
    futures = [executor.submit(generate_mesh_batch, batch_id, batch) for batch_id, batch in zip(batch_ids, batches)]
    for future in futures:
        future.result()

# End the timer
end_time = time.time()

# Print the total time taken
print("Total time taken:", end_time - start_time, "seconds")