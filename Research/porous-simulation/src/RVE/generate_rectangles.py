# Author: Jake Heairld
# Modified for rectangular pores with L0 scaling
import gmsh
import os
import sys
import random
import math
from shapely.geometry import Polygon, box
import json
import argparse

# ==============================
# Centralized Configuration
# ==============================
CONFIG = {
    # Fixed domain parameters (mm)
    "domain_width": 10.0,
    "domain_height": 10.0,

    # Porosity control
    "target_porosity": 0.3,
    "max_attempts": 5000,

    # Absolute pore dimensions (mm)
    "min_pore_width": 0.2,
    "max_pore_width": 0.5,
    "min_pore_height": 1.0,
    "max_pore_height": 4.0,

    # Minimum distance between pores and walls (mm)
    "min_dist": 0.25,
    "min_rotation": 0,
    "max_rotation": math.pi / 2,

    # L0-controlled parameters
    "lc": 5,
    "cm": 10
}

# ==============================
# Pore Class Definition
# ==============================
class Pore:
    def __init__(self, x, y, width, height, angle):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.angle = angle
        self.polygon = self.create_polygon()

    def create_polygon(self):
        w = self.width / 2
        h = self.height / 2
        corners = [(-w, -h), (w, -h), (w, h), (-w, h)]
        return Polygon([self.rotate_point(x, y) for x, y in corners])

    def rotate_point(self, x, y):
        cos_a = math.cos(self.angle)
        sin_a = math.sin(self.angle)
        return (
            x * cos_a - y * sin_a + self.x,
            x * sin_a + y * cos_a + self.y
        )

# ==============================
# Core Functions
# ==============================
def generate_pores(save_config=False, config_file="pore_config.json"):
    """Generate pores with constant dimensions"""
    cfg = CONFIG

    if os.path.exists(config_file):
        with open(config_file, 'r') as f:
            data = json.load(f)
            pores = [Pore(**p) for p in data]
            print("Loaded existing pore configuration")
            return pores

    print("\nGenerating new pores:")
    domain = box(0, 0, cfg["domain_width"], cfg["domain_height"])
    total_area = cfg["domain_width"] * cfg["domain_height"]
    target_area = total_area * cfg["target_porosity"]

    pores = []
    current_area = 0
    attempts = 0
    random.seed(42)

    while current_area < target_area and attempts < cfg["max_attempts"]:
        width = random.uniform(cfg["min_pore_width"], cfg["max_pore_width"])
        height = random.uniform(cfg["min_pore_height"], cfg["max_pore_height"])
        angle = random.uniform(cfg["min_rotation"], cfg["max_rotation"])

        # Calculate safe placement boundaries
        max_half_dim = max(width/2, height/2)
        safe_margin = max_half_dim + cfg["min_dist"]
        x = random.uniform(safe_margin, cfg["domain_width"] - safe_margin)
        y = random.uniform(safe_margin, cfg["domain_height"] - safe_margin)

        pore = Pore(x, y, width, height, angle)
        buffer_zone = pore.polygon.buffer(cfg["min_dist"])

        if not domain.contains(buffer_zone):
            attempts += 1
            continue

        if any(buffer_zone.intersects(p.polygon) for p in pores):
            attempts += 1
            continue

        pores.append(pore)
        current_area += pore.polygon.area
        attempts = 0

    print(f"Generated {len(pores)} pores ({current_area/total_area:.1%} porosity)")

    if save_config:
        pore_data = [{'x': p.x, 'y': p.y, 'width': p.width,
                     'height': p.height, 'angle': p.angle} for p in pores]
        with open(config_file, 'w') as f:
            json.dump(pore_data, f)
        print(f"Saved pore configuration to {config_file}")

    return pores

def generate_mesh():
    """Generate mesh with consistent pores"""
    parser = argparse.ArgumentParser()
    parser.add_argument("L0", type=float)
    parser.add_argument("--save", help="Save pore configuration")
    parser.add_argument("--load", help="Load pore configuration")
    args = parser.parse_args()

    gmsh.initialize()
    gmsh.option.setNumber("General.Terminal", 1)
    gmsh.model.add(f"L0_{args.L0}")

    try:
        pores = generate_pores(
            save_config=args.save is not None,
            config_file=args.save if args.save else args.load
        )

        # L0-controlled parameters
        lc = CONFIG["lc"] / args.L0
        cm = CONFIG["cm"] / args.L0

        # Create domain
        points = [
            gmsh.model.geo.addPoint(0, 0, 0, cm),
            gmsh.model.geo.addPoint(CONFIG["domain_width"], 0, 0, cm),
            gmsh.model.geo.addPoint(CONFIG["domain_width"], CONFIG["domain_height"], 0, cm),
            gmsh.model.geo.addPoint(0, CONFIG["domain_height"], 0, cm)
        ]

        lines = [
            gmsh.model.geo.addLine(points[0], points[1]),
            gmsh.model.geo.addLine(points[1], points[2]),
            gmsh.model.geo.addLine(points[2], points[3]),
            gmsh.model.geo.addLine(points[3], points[0])
        ]

        main_loop = gmsh.model.geo.addCurveLoop(lines)
        curve_loops = [main_loop]

        # Add pores
        for pore in pores:
            pore_points = []
            for coord in pore.polygon.exterior.coords[:-1]:
                pore_points.append(gmsh.model.geo.addPoint(coord[0], coord[1], 0, lc))

            pore_lines = [
                gmsh.model.geo.addLine(pore_points[0], pore_points[1]),
                gmsh.model.geo.addLine(pore_points[1], pore_points[2]),
                gmsh.model.geo.addLine(pore_points[2], pore_points[3]),
                gmsh.model.geo.addLine(pore_points[3], pore_points[0])
            ]

            curve_loops.append(gmsh.model.geo.addCurveLoop(pore_lines))

        surface = gmsh.model.geo.addPlaneSurface(curve_loops)
        gmsh.model.geo.synchronize()

        # Physical groups
        gmsh.model.addPhysicalGroup(2, [surface], 1)
        gmsh.model.addPhysicalGroup(1, lines, 2)

        # Generate mesh
        gmsh.model.mesh.generate(2)

        # Save mesh
        mesh_dir = os.path.join("meshes", f"L0_{args.L0}")
        os.makedirs(mesh_dir, exist_ok=True)
        gmsh.write(os.path.join(mesh_dir, "mesh1.msh"))

    except Exception as e:
        print(f"Mesh generation failed: {str(e)}")
    finally:
        gmsh.clear()
        gmsh.finalize()

if __name__ == "__main__":
    generate_mesh()