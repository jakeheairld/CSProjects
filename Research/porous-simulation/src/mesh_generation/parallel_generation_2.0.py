import time
import gmsh
import XdmfConverter
import PoreGenerator
from concurrent.futures import ProcessPoolExecutor

# Start the timer
start_time = time.time()

# Parameters
pore_type = "circle"
uses_fields = False
num_pores = 7
radius_min = 0.05
radius_max = 0.15
plane_width = 1
plane_height = 1
cm = 1e-2
lc = 1e-1/2
num_models = 20

# Generate the models
models = [PoreGenerator.generate_pores(num_pores, radius_min, radius_max, plane_width, plane_height)
          for _ in range(num_models)]

# Function to generate mesh for each model
def generate_mesh(model_id, pores, uses_fields):
    try:
        gmsh.initialize()
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
        for i in range(len(pores)):
            pore = pores[i]
            if uses_fields:
                left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, lc)
                center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, lc)
                right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, lc)
            else:
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

        if uses_fields:
            # Create a field to control mesh refinement based on distance
            field_id = gmsh.model.mesh.field.add("Distance")
            gmsh.model.mesh.field.setNumbers(field_id, "CurvesList", [5 + i for i in range(2 * len(curve_loops))])

            # Threshold to transition between fine and coarse mesh
            thresh_field = gmsh.model.mesh.field.add("Threshold")
            gmsh.model.mesh.field.setNumber(thresh_field, "InField", field_id)
            gmsh.model.mesh.field.setNumber(thresh_field, "LcMin", cm / 3)  # Fine mesh size near the circle
            gmsh.model.mesh.field.setNumber(thresh_field, "LcMax", lc)      # Coarser mesh further away
            gmsh.model.mesh.field.setNumber(thresh_field, "DistMin", 0)  # Distance where fine mesh begins
            gmsh.model.mesh.field.setNumber(thresh_field, "DistMax", 0.01)  # Distance where transitions to coarser mesh

            # Apply the defined field as the background mesh size
            gmsh.model.mesh.field.setAsBackgroundMesh(thresh_field)

        gmsh.model.mesh.generate(2)
        mesh_file = f"../SavedMeshFiles/mesh{model_id + 1}.msh"
        gmsh.write(mesh_file)
        XdmfConverter.convert_msh_to_xdmf(mesh_file, f"../SavedXdmfFiles/mesh{model_id + 1}.xdmf")
        gmsh.finalize()

        print(f"Mesh {model_id + 1} generated successfully.")

    except Exception as e:
        print(f"Error generating mesh {model_id + 1}: {e}")
        gmsh.finalize()

if __name__ == "__main__":
    # Use ProcessPoolExecutor to run mesh generation in parallel
    with ProcessPoolExecutor() as executor:
        futures = [executor.submit(generate_mesh, m, models[m], uses_fields) for m in range(num_models)]

    # Wait for all futures to complete
    for future in futures:
        future.result()  # Block until all tasks are done

    # End the timer
    end_time = time.time()

    # Get the total run time
    run_time = end_time - start_time

    print(run_time)

    print("All mesh generation tasks completed.")