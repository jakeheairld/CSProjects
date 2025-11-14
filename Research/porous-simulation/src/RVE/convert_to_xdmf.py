from scripts.mesh_generation.converters import xdmf_converter
import os

output_dir = "../../datasets/meshes/msh"
os.makedirs(output_dir, exist_ok=True)

output_xdmf_dir = "../../datasets/meshes/xdmf"
os.makedirs(output_xdmf_dir, exist_ok=True)
xdmf_converter.convert_msh_to_xdmf(
    "meshes/homogeneous_meshes/mesh1.msh",
    "meshes/xdmf_meshes/mesh1.xdmf"
)