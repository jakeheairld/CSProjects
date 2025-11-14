from skimage.measure import regionprops, label
import numpy as np
import porespy as ps
import matplotlib.pyplot as plt
import time
import multiprocessing

def generate_pores(num_pores, plane_width, plane_height, radius_min, radius_max):
    # Generate a synthetic porous medium using PoreSpy
    im = ps.generators.blobs(
        shape=[int(plane_height * 100), int(plane_width * 100)],
        porosity=1 - (np.pi * (radius_max ** 2) * num_pores) / (plane_width * plane_height),
        blobiness=2
    )

    # Show the generated image to verify
    plt.imshow(im, cmap='gray')
    plt.title('Generated Image')
    plt.show()

    # Label connected regions (blobs)
    labeled_image = label(im)

    # Get region properties
    props = regionprops(labeled_image)

    # Filter out pores outside the desired radius range
    pores = []
    for prop in props:
        x, y = prop.centroid  # Now x, y only (2D image)
        r = np.cbrt((3 * prop.area) / (4 * np.pi))
        if radius_min <= r <= radius_max:
            pores.append((x / 100, y / 100, r))  # Store the pores

    # Limit the number of pores
    return pores[:num_pores]

# Parallel function to generate and visualize pores
def generate_model(model_index, num_pores, radius_min, radius_max, plane_width, plane_height):
    pores = generate_pores(num_pores, plane_width, plane_height, radius_min, radius_max)

# Main function for parallel execution
if __name__ == "__main__":
    # Parameters
    num_pores = 6
    plane_width = 1  # meters
    plane_height = 1  # meters
    num_models = 1
    radius_min = 0.1  # meters
    radius_max = 0.15  # meters

    # Start timing
    start_time = time.time()

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.starmap(
            generate_model,
            [(i, num_pores, radius_min, radius_max, plane_width, plane_height) for i in range(num_models)],
        )

    # End timing
    end_time = time.time()

    # Print total time taken
    print(f"Total time taken to generate all models: {end_time - start_time:.2f} seconds")
