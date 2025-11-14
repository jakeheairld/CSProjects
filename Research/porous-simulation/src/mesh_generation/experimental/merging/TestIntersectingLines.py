import gmsh

# Parameters
cm = 0.1
lc = 0.5

# Initialize gmsh
gmsh.initialize()

# Define circles (pores)
pores = [
    (4.0, 5.0, 2.0),  # First pore
    (6.0, 5.0, 2.0)  # Second pore
]

curve_loops = []
# Create circles and subtract from the square
x = pores[0][0]
x1 = pores[1][0]
y = pores[0][1]
y1 = pores[1][1]
r = pores[0][2]
r1 = pores[1][2]

center = gmsh.model.occ.addPoint(x, y, 0, cm)
left = gmsh.model.occ.addPoint(x - r, y, 0, cm)
right = gmsh.model.occ.addPoint(x + r, y, 0, cm)
arc1 = gmsh.model.occ.addCircleArc(left, center, right)
arc2 = gmsh.model.occ.addCircleArc(right, center, left)
loop = gmsh.model.occ.addCurveLoop([arc1, arc2])
curve_loops.append(loop)

center = gmsh.model.occ.addPoint(x1, y1, 0, cm)
left = gmsh.model.occ.addPoint(x1 - r1, y1, 0, cm)
right = gmsh.model.occ.addPoint(x1 + r1, y1, 0, cm)
arc1 = gmsh.model.occ.addCircleArc(left, center, right)
arc2 = gmsh.model.occ.addCircleArc(right, center, left)
loop2 = gmsh.model.occ.addCurveLoop([arc1, arc2])
curve_loops.append(loop2)

# Synchronize
gmsh.model.occ.synchronize()

# Now let's check for intersections between the two lines
intersection = gmsh.model.occ.intersect(
    [(1, loop)],  # First line (vertical)
    [(1, loop2)]  # Second line (horizontal)
)

# Check the intersection result
if intersection:
    print(f"The lines intersect")
    print(intersection)
else:
    print("The lines do not intersect.")

# Generate the mesh
gmsh.model.mesh.generate(2)

# Write the mesh to a file
gmsh.write("MergedPores.msh")

# Show the GUI
gmsh.fltk.run()

# Finalize gmsh
gmsh.finalize()
