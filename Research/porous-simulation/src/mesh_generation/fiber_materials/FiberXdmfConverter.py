import meshio

def convert_msh_to_xdmf(input_file, output_file):
    # Read the .msh file
    mesh = meshio.read(input_file)

    # Extract points and cells
    points = mesh.points
    cells = mesh.cells_dict
    cell_data = mesh.cell_data_dict

    # Ensure the points are 2D by dropping the z-coordinate
    if points.shape[1] == 3:  # If points are 3D
        points = points[:, :2]  # Keep only x and y coordinates

    # Create a new mesh with only triangles and their data
    triangle_mesh = meshio.Mesh(
        points=points,
        cells={"triangle": cells["triangle"]},
        cell_data={
            "gmsh:geometrical": [cell_data["gmsh:geometrical"]["triangle"]]
        }
    )

    # Write the .xdmf file
    meshio.write(output_file, triangle_mesh)


