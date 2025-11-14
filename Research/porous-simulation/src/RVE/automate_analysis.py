import os
import subprocess
import numpy as np
import matplotlib.pyplot as plt
import time
import uuid

mesh_generator = "generate_single_circle.py"
L0_values = [1, 2, 4, 8, 16]  # Test values for L0
results = []
timing_data = []

# Generate unique pore configuration for this run
run_id = uuid.uuid4().hex[:8]  # Unique 8-character ID
current_pores = f"pores_{run_id}.json"

# Generate base pore configuration once per run
print(f"Generating new pore configuration: {current_pores}")
subprocess.run(["python", mesh_generator, "1", "--save", current_pores], check=True)

# Warm-up run with new configuration
print("Performing warm-up run...")
subprocess.run(["python", mesh_generator, "1"], check=True)
subprocess.run(["python", "calculate_ceff.py", "meshes/L0_1.0/mesh1.msh", "results/warmup"], check=True)
print("Warm-up complete. Starting timed runs...\n")

for L0 in L0_values:
    # Track timing for this L0
    L0_times = {'L0': L0, 'mesh_time': 0, 'calc_time': 0}

    # Generate mesh with timing
    mesh_dir = os.path.join("meshes", f"L0_{float(L0):.1f}")
    start_mesh = time.time()
    subprocess.run(["python", mesh_generator, str(L0), "--load", current_pores], check=True)
    L0_times['mesh_time'] = time.time() - start_mesh

    # Run calculation with timing
    result_dir = os.path.join("results", f"L0_{float(L0):.1f}")
    start_calc = time.time()
    subprocess.run([
        "python", "calculate_ceff.py",
        os.path.join(mesh_dir, "mesh1.msh"),
        result_dir
    ], check=True)
    L0_times['calc_time'] = time.time() - start_calc

    # Store results
    C_hom = np.loadtxt(os.path.join(result_dir, "C_hom.txt"))
    results.append((L0, C_hom))
    timing_data.append(L0_times)

# Plotting function
def plot_convergence(results):
    plt.figure(figsize=(12, 7))
    markers = ['o', 's', '^']  # Circle, square, triangle
    components = [(0, 0), (1, 1), (0, 1)]  # C11, C22, C12
    labels = ['$C_{11}$', '$C_{22}$', '$C_{12}$']

    # Sort results by L0
    results.sort(key=lambda x: x[0])
    L0s = np.array([r[0] for r in results])

    # Plot individual components with straight lines
    for idx, (i, j) in enumerate(components):
        vals = np.array([r[1][i, j] for r in results])
        plt.semilogx(L0s, vals, marker=markers[idx],
                     linestyle='-', linewidth=2,
                     markersize=8, markeredgecolor='k',
                     label=labels[idx])

    # Calculate and smooth combined average
    combined = np.array([(C[0, 0] + C[1, 1] + C[0, 1]) / 3 for _, C in results])
    plt.semilogx(L0s, combined, marker='d', linestyle='--',
                 color='k', linewidth=2, markersize=8,
                 markeredgecolor='k', label='Combined Average')

    # Improved axis formatting
    plt.xticks(L0_values,
               [f'L0 = {x:.0f}' for x in L0_values],
               rotation=35, ha='right',
               fontsize=10)
    plt.yticks(fontsize=10)

    plt.xlabel('Mesh Refinement Parameter (L0)',
               fontsize=12, labelpad=12)
    plt.ylabel('Stiffness Component Value (Pa)',
               fontsize=12, labelpad=12)
    plt.title('Stiffness Component Convergence',
              fontsize=14, pad=18)

    # Adjust legend position
    plt.legend(loc='upper center',
               bbox_to_anchor=(0.5, -0.25),
               ncol=2, fontsize=10)

    plt.grid(True, which='both', alpha=0.3)
    plt.tight_layout(pad=3.0)
    plt.savefig(os.path.join("results", "convergence_plot.png"),
                dpi=300, bbox_inches='tight')
    plt.show()

# Computation time plot function
def plot_computation_times(timing_data):
    plt.figure(figsize=(12, 7))

    # Extract and process timing data
    L0s = np.array([d['L0'] for d in timing_data])
    total_times = np.array([(d['mesh_time'] + d['calc_time'])*1000 for d in timing_data])

    # Create log-scaled x values
    log_L0s = np.log(L0s)

    # Calculate axis limits with padding
    y_min = np.min(total_times) * 0.95
    y_max = np.max(total_times) * 1.15

    # Plot actual data points with connecting line
    plt.semilogx(L0s, total_times, 'bo-', markersize=10,
                 linewidth=2, label='Total Time',
                 markerfacecolor='white', markeredgewidth=2)

    # Calculate and plot trend line
    coefficients = np.polyfit(log_L0s, total_times, 1)
    trend_line = np.poly1d(coefficients)
    plt.semilogx(L0s, trend_line(log_L0s), 'r--', linewidth=2,
                 label=f'Trend: y={coefficients[0]:.1f}log(x) + {coefficients[1]:.1f}')

    # Formatting
    plt.xticks(L0s, [f'L0={x}' for x in L0s],
               rotation=35, fontsize=12)
    plt.yticks(np.linspace(y_min, y_max, 5), fontsize=12)
    plt.ylim(y_min, y_max)

    plt.xlabel('Mesh Refinement Parameter (L0)',
               fontsize=14, labelpad=15)
    plt.ylabel('Computation Time (ms)',
               fontsize=14, labelpad=15)
    plt.title('Computation Time Analysis',
              fontsize=16, pad=20)

    # Add data labels
    for x, y in zip(L0s, total_times):
        plt.text(x, y, f'{y:.1f} ms',
                 ha='center', va='bottom',
                 fontsize=11, bbox=dict(facecolor='white', alpha=0.8))

    plt.legend(loc='upper left', fontsize=12)
    plt.grid(True, which='both', alpha=0.2)
    plt.tight_layout()
    plt.savefig(os.path.join("results", "time_analysis.png"),
                dpi=300, bbox_inches='tight')
    plt.show()

# Run both plots
plot_convergence(results)
plot_computation_times(timing_data)