import gmsh

# Parameters
plane_width = 10
plane_height = 10
cm = 0.1
lc = 0.5

# Initialize gmsh
gmsh.initialize()

# Create a square uniform mesh with uniform mesh size
gmsh.model.occ.addPoint(0, 0, 0, lc, 1)
gmsh.model.occ.addPoint(plane_width, 0, 0, lc, 2)
gmsh.model.occ.addPoint(plane_width, plane_height, 0, lc, 3)
gmsh.model.occ.addPoint(0, plane_height, 0, lc, 4)
gmsh.model.occ.addLine(1, 2, 1)
gmsh.model.occ.addLine(3, 2, 2)
gmsh.model.occ.addLine(3, 4, 3)
gmsh.model.occ.addLine(4, 1, 4)
gmsh.model.occ.addCurveLoop([1, -2, 3, 4], 1)

# Initialize list to store curve loops for the main surface and pores
curve_loops = [1]

# Define circles (pores)
pores = [
    (4.0, 5.0, 2.0),  # First pore
    (6.0, 5.0, 2.0)  # Second pore
]

pore_loops = []
# Create circles and subtract from the square
for idx, (x, y, r) in enumerate(pores):
    center = gmsh.model.occ.addPoint(x, y, 0, cm)
    left = gmsh.model.occ.addPoint(x - r, y, 0, cm)
    right = gmsh.model.occ.addPoint(x + r, y, 0, cm)
    arc1 = gmsh.model.occ.addCircleArc(left, center, right)
    arc2 = gmsh.model.occ.addCircleArc(right, center, left)
    loop = gmsh.model.occ.addCurveLoop([arc1, arc2])
    curve_loops.append(loop)


gmsh.model.occ.addPlaneSurface([2])
gmsh.model.occ.addPlaneSurface([3])
gmsh.model.occ.addPlaneSurface([1])

#gmsh.model.occ.addPlaneSurface(curve_loops)

gmsh.model.occ.synchronize()

# Create mesh
gmsh.model.mesh.generate(2)

# Write to file
gmsh.write("MergedPores.msh")

# Show the GUI
gmsh.fltk.run()

# Finalize gmsh
gmsh.finalize()
