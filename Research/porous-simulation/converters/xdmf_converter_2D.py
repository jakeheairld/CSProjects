import meshio
import numpy as np  # Import numpy as np


def convert_msh_to_xdmf(input_file, output_file):
    # Read the .msh file
    mesh = meshio.read(input_file)

    # Extract points and cells
    points = mesh.points
    cells = mesh.cells_dict
    cell_data = mesh.cell_data_dict

    # If cell data contains 'gmsh:geometrical', check its structure
    if 'gmsh:geometrical' in cell_data:
        geom_data = cell_data['gmsh:geometrical']

        # 'geom_data' is expected to be a dictionary, so we handle it accordingly
        if isinstance(geom_data, dict):
            # You can check if the length of 'geom_data' matches the number of cells of a certain type (e.g., triangles)
            num_cells = len(cells.get("triangle", []))  # Use get to avoid KeyError
            num_geom_data = len(geom_data)

            if num_geom_data != num_cells:
                print(f"Warning: The geometrical data has {num_geom_data} blocks, but cells have {num_cells} blocks.")
                # Handle this mismatch by either adjusting geom_data or removing it
                del cell_data['gmsh:geometrical']  # Optionally remove 'gmsh:geometrical' if not needed

    # Create a new mesh with only triangles and their data
    triangle_mesh = meshio.Mesh(
        points=points,
        cells={"triangle": cells.get("triangle", [])},  # Use get to avoid KeyError
        cell_data=cell_data
    )

    # Write the .xdmf file
    meshio.write(output_file, triangle_mesh)


