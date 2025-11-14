import meshio
import matplotlib.pyplot as plt
import matplotlib.collections as mc

def convert_msh_to_png(input_msh_path, output_png_path, dpi=300):
    # Read the Gmsh .msh file
    mesh = meshio.read(input_msh_path)

    # Collect all 2D polygons (triangles, quads)
    polygons = []
    for cell_block in mesh.cells:
        if cell_block.type in ["triangle", "quad"]:
            # Extract 2D points (drop z-coordinate if present)
            points_2d = mesh.points[:, :2]
            polygons.extend(points_2d[cell][:, :2] for cell in cell_block.data)

    # Plotting setup
    fig, ax = plt.subplots(figsize=(8, 8))  # Square figure to match aspect ratio
    coll = mc.PolyCollection(polygons, edgecolor="black", facecolor="black")
    ax.add_collection(coll)

    # Set equal aspect ratio and remove axes
    ax.set_aspect("equal")  # Critical fix for stretching
    ax.autoscale_view()
    ax.set_axis_off()

    # Save as PNG with tight bounding box
    plt.savefig(output_png_path, bbox_inches="tight", pad_inches=0, dpi=dpi)
    plt.close()