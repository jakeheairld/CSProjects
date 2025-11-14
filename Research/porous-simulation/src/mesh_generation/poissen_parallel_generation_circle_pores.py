import math
import multiprocessing
import gmsh
import XdmfConverter
import os
import time
import numpy as np
import random

minDist = 0.025  # Minimum distance between pores

# Circle pore class
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r
        self.type = 'Circle'

    # Method to check if two circles overlap
    def overlaps(self, other_pore):
        distance = math.sqrt((self.x - other_pore.x) ** 2 + (self.y - other_pore.y) ** 2)
        return distance < (self.r + other_pore.r + minDist)

def poisson_disk_sampling(width, height, r_min, r_max, n_pores, k=30):
    def get_cell_coords(pt):
        return int(pt[0] // cell_size), int(pt[1] // cell_size)

    def get_neighbours(coords):
        return [(coords[0] + dx, coords[1] + dy) for dx in [-1, 0, 1] for dy in [-1, 0, 1]]

    def point_valid(pt, radius):
        if not (radius <= pt[0] < width - radius and radius <= pt[1] < height - radius):
            return False
        cell_coords = get_cell_coords(pt)
        for nx, ny in get_neighbours(cell_coords):
            if 0 <= nx < grid_width and 0 <= ny < grid_height:
                if grid[nx][ny] is not None:
                    nearby_point, nearby_radius = grid[nx][ny]
                    if np.linalg.norm(np.array(pt) - np.array(nearby_point)) < (radius + nearby_radius + minDist):
                        return False
        return True

    cell_size = r_max * 2 + minDist
    grid_width, grid_height = int(np.ceil(width / cell_size)), int(np.ceil(height / cell_size))
    grid = [[None for _ in range(grid_height)] for _ in range(grid_width)]

    pores = []
    active = []

    while len(pores) < n_pores:
        if not active:
            radius = random.uniform(r_min, r_max)
            x = random.uniform(radius, width - radius)
            y = random.uniform(radius, height - radius)
            if point_valid((x, y), radius):
                pores.append(Pore(x, y, radius))
                active.append((x, y))
                grid[get_cell_coords((x, y))[0]][get_cell_coords((x, y))[1]] = ((x, y), radius)
        else:
            idx = random.randint(0, len(active) - 1)
            point = active[idx]
            for _ in range(k):
                angle = random.uniform(0, 2 * np.pi)
                new_radius = random.uniform(r_min, r_max)
                r = random.uniform(2 * max(r_max, new_radius), 3 * max(r_max, new_radius))
                new_point = (point[0] + r * np.cos(angle), point[1] + r * np.sin(angle))
                if point_valid(new_point, new_radius):
                    pores.append(Pore(new_point[0], new_point[1], new_radius))
                    active.append(new_point)
                    grid[get_cell_coords(new_point)[0]][get_cell_coords(new_point)[1]] = (new_point, new_radius)
                    break
            else:
                active.pop(idx)

    return pores


def generate_pores_poisson(n_pores, r_min, r_max, w_width, w_height):
    def is_valid_position(x, y, r):
        if x - r - minDist < 0 or x + r + minDist > w_width or y - r - minDist < 0 or y + r + minDist > w_height:
            return False
        if pores:
            distances = np.sqrt(np.sum((np.array(pores) - [x, y, 0]) ** 2, axis=1))
            return np.all(distances > (r + np.array(radii) + minDist))
        return True

    pores = []
    radii = []
    attempts = 0
    max_attempts = 1000 * n_pores

    while len(pores) < n_pores and attempts < max_attempts:
        r = random.uniform(r_min, r_max)
        x = random.uniform(r + minDist, w_width - r - minDist)
        y = random.uniform(r + minDist, w_height - r - minDist)

        if is_valid_position(x, y, r):
            pores.append([x, y, 0])  # Adding z=0 for compatibility with cKDTree
            radii.append(r)

        attempts += 1

    if len(pores) < n_pores:
        print(f"Warning: Could only place {len(pores)} pores out of {n_pores} requested.")

    return [Pore(x, y, r) for (x, y, _), r in zip(pores, radii)]


# Function to generate both pores and meshes for a batch of models
def generate_models_batch(args):
    batch, batch_size, num_pores, r_min, r_max, plane_width, plane_height, lc, cm = args
    gmsh.initialize()
    for model_index in batch:

        gmsh.model.add(f"model{model_index + 1}")
        # Generate pores for this model
        pores = generate_pores_poisson(num_pores, r_min, r_max, plane_width, plane_height)

        # Create square geometry
        gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
        gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
        gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
        gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
        gmsh.model.geo.addLine(1, 2, 1)
        gmsh.model.geo.addLine(3, 2, 2)
        gmsh.model.geo.addLine(3, 4, 3)
        gmsh.model.geo.addLine(4, 1, 4)
        gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

        # Define pores as circles and create curve loops for each
        curve_loops = [1]
        for i, pore in enumerate(pores):
            left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
            center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
            right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

            gmsh.model.geo.addCircleArc(left, center, right, 5 + 2 * i)
            gmsh.model.geo.addCircleArc(right, center, left, 6 + 2 * i)
            gmsh.model.geo.addCurveLoop([5 + 2 * i, 6 + 2 * i], 2 + i)
            curve_loops.append(2 + i)

        surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
        gmsh.model.geo.synchronize()

        # Generate mesh
        gmsh.model.mesh.generate(2)
        output_dir = "../SavedMeshFiles"
        os.makedirs(output_dir, exist_ok=True)
        gmsh.write(f"{output_dir}/mesh{model_index + 1}.msh")

        # Convert to XDMF format
        output_xdmf_dir = "../SavedXdmfFiles"
        os.makedirs(output_xdmf_dir, exist_ok=True)
        XdmfConverter.convert_msh_to_xdmf(f"{output_dir}/mesh{model_index + 1}.msh",
                                          f"{output_xdmf_dir}/mesh{model_index + 1}.xdmf")

        gmsh.model.remove()
    gmsh.finalize()

# Main function for parallel execution
if __name__ == "__main__":
    # Parameters
    num_pores = 9
    plane_width = 1
    plane_height = 1
    cm = 1e-2
    lc = 1e-1 / 2
    num_models = 50
    batch_size = 10  # Adjust batch size for parallel processing

    # Circle pores
    radius_min = 0.10
    radius_max = 0.15

    # Start timing
    start_time = time.time()

    # Split models into batches for parallel processing
    batches = [list(range(i, min(i + batch_size, num_models))) for i in range(0, num_models, batch_size)]

    # Create a process pool for parallel execution
    with multiprocessing.Pool() as pool:
        pool.map(generate_models_batch, [(batch, batch_size, num_pores, radius_min, radius_max, plane_width, plane_height, lc, cm) for batch in batches])

    # End timing
    end_time = time.time()

    # Print total time taken
    print(f"Total time taken to generate all meshes: {end_time - start_time:.2f} seconds")