# Author: Jake Heairld
# Last Updated: 11/29/24
#
# Description: This script converts a GMSH mesh file (.msh) into the XDMF format (.xdmf)
# for compatibility with other simulation tools. It reads the mesh data, extracts
# the 2D points and triangle cells, and ensures that the mesh is in 2D by discarding
# any z-coordinate. The script then creates a new mesh with the relevant triangle cells
# and associated geometrical data before saving it in the XDMF format.

import meshio

# Function to convert a .msh (GMSH mesh) file to an .xdmf file
def convert_msh_to_xdmf(input_file, output_file):
    # Read the .msh file using meshio
    mesh = meshio.read(input_file)

    # Extract points and cells from the mesh
    points = mesh.points  # The coordinates of the mesh points
    cells = mesh.cells_dict  # The cell connectivity (e.g., triangles, quadrilaterals)
    cell_data = mesh.cell_data_dict  # Additional data associated with the cells (e.g., physical groups, geometrical information)

    # Ensure the points are 2D by dropping the z-coordinate if the mesh is 3D
    if points.shape[1] == 3:  # Check if the mesh is 3D (has three coordinates: x, y, z)
        points = points[:, :2]  # Keep only the x and y coordinates, discard the z-coordinate

    # Create a new mesh with only the 2D triangle cells and their associated data
    triangle_mesh = meshio.Mesh(
        points=points,  # Use the updated 2D points
        cells={"triangle": cells["triangle"]},  # Keep only triangle cells
        cell_data={
            "gmsh:geometrical": [cell_data["gmsh:geometrical"]["triangle"]]  # Keep the geometrical data for triangles
        }
    )

    # Write the new mesh in .xdmf format to the output file
    meshio.write(output_file, triangle_mesh)  # Save the mesh as .xdmf