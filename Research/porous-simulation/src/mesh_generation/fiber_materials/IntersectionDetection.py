import random
import math
import numpy as np
import matplotlib.pyplot as plt
from shapely.geometry import Polygon, box

class Pore:
    def __init__(self, x, y, width, height, angle):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.angle = angle
        self.polygon = self.create_polygon()

    def create_polygon(self):
        w, h = self.width / 2, self.height / 2
        corners = [
            (-w, -h), (w, -h), (w, h), (-w, h)
        ]
        rotated_corners = [self.rotate_point(x, y) for x, y in corners]
        return Polygon(rotated_corners)

    def rotate_point(self, x, y):
        cos_a = math.cos(self.angle)
        sin_a = math.sin(self.angle)
        rx = x * cos_a - y * sin_a + self.x
        ry = x * sin_a + y * cos_a + self.y
        return (rx, ry)


def generate_pores(n_pores, w_min, w_max, h_min, h_max, w_width, w_height, target_pore_area_ratio, min_dist):
    cell_size = min(w_min, h_min) / 2
    grid_width = int(w_width / cell_size)
    grid_height = int(w_height / cell_size)
    grid = np.zeros((grid_height, grid_width), dtype=bool)

    pores = []
    total_area = w_width * w_height
    current_pore_area = 0
    target_pore_area = total_area * target_pore_area_ratio

    plane = box(0, 0, w_width, w_height)

    max_attempts = 100000  # Reduced max attempts
    attempts = 0

    while len(pores) < n_pores and current_pore_area < target_pore_area and attempts < max_attempts:
        x = random.uniform(0, w_width)
        y = random.uniform(0, w_height)
        width = random.uniform(w_min, w_max)
        height = random.uniform(h_min, h_max)
        angle = random.uniform(0, math.pi)

        new_pore = Pore(x, y, width, height, angle)

        if not plane.contains(new_pore.polygon.buffer(min_dist)):
            attempts += 1
            continue

        if any(new_pore.polygon.buffer(min_dist).intersects(pore.polygon) for pore in pores):
            attempts += 1
            continue

        pores.append(new_pore)
        current_pore_area += width * height
        attempts += 1

    print(f"Generated {len(pores)} pores")
    print(f"Pore area ratio: {current_pore_area / total_area:.2f}")
    print(f"Total attempts: {attempts}")

    return pores


def plot_pores(pores, w_width, w_height):
    fig, ax = plt.subplots()
    for pore in pores:
        x, y = pore.polygon.exterior.xy
        ax.plot(x, y)

    ax.set_xlim(0, w_width)
    ax.set_ylim(0, w_height)
    ax.set_aspect('equal', 'box')
    plt.title("Generated Pores")
    plt.show()

if __name__ == "__main__":
    num_pores = 20
    plane_width = 1.5
    plane_height = 1
    target_pore_area_ratio = 0.1
    min_dist = 0.025

    width_min = 0.02
    width_max = 0.06
    height_min = 0.08
    height_max = 0.40

    pores = generate_pores(num_pores, width_min, width_max, height_min, height_max, plane_width, plane_height, target_pore_area_ratio, min_dist)
    plot_pores(pores, plane_width, plane_height)
