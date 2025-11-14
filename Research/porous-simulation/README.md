# Porous Material Simulation & Mesh Generation Toolkit

Tools for generating porous microstructures, constructing RVEs, generating meshes, and computing CEFF values for fracture prediction research.

Developed as part of my work as an Undergraduate Research Assistant in the Integrated Multi-Physics Laboratory at the University of Utah.

---

## Overview

This toolkit provides a modular set of Python scripts for:

- Generating pores of various geometries (circles, rectangles, ellipses, irregular shapes)
- Constructing Representative Volume Elements (RVEs)
- Generating finite-element meshes
- Performing CEFF (Critical Effective Fracture Factor) calculations used in fracture analysis
- Running mesh sensitivity studies
- Converting between common geometry/mesh file formats (PNG → mesh, XDMF, etc.)
- Automating preprocessing and analysis workflows

These tools support ongoing computational research in porous material behavior and remain in active use in the lab.

---

## Project Structure

```text
porous-simulation/
│
├── src/
│   ├── RVE/                        # RVE construction and CEFF workflows
│   ├── pores/                      # Pore generation utilities
│   ├── mesh_generation/            # Mesh construction tools
│   └── analysis/                   # Sensitivity and stress analysis
│
├── converters/                     # PNG/XDMF and mesh format converters
├── utilities/                      # Config files and helper modules
├── examples/                       # Example notebooks and demonstration scripts
├── requirements.txt
└── README.md
```

---

## Installation

```bash
pip install -r requirements.txt
```

---

## Example Usage

Generate a circular pore RVE:

```bash
python src/simple_pores/generate_circular_pores.py
```

Compute CEFF:

```bash
python src/RVE/calculate_ceff.py
```

---

## Notes

- This repository includes only the portions of my research code that can be shared publicly.
- Internal datasets, unpublished models, and restricted analysis workflows have been intentionally excluded.
- Scripts are organized and structured to reflect professional, maintainable engineering practices.
- Some of the scripts may require some extra work in order to interact correctly with one another.

---

If you have questions about specific scripts or workflow details, feel free to reach out!
