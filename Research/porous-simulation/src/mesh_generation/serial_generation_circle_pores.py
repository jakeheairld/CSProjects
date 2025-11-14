import gmsh
import XdmfConverter
import PoreGenerator

# Parameters
num_pores = 7
plane_width = 1
plane_height = 1
cm = 1e-2
lc = 1e-1/2
num_models = 3

# Circle pores
radius_min = 0.10
radius_max = 0.15

models = [PoreGenerator.generate_pores(num_pores, radius_min, radius_max, plane_width, plane_height)
              for _ in range(num_models)]

# Initialize gmsh
gmsh.initialize()

for m in range(num_models):
    gmsh.model.add("model" + str(m+1))
    pores = models[m]

    # Create a square uniform mesh with uniform mesh size
    gmsh.model.geo.addPoint(0, 0, 0, lc, 1)
    gmsh.model.geo.addPoint(plane_width, 0, 0, lc, 2)
    gmsh.model.geo.addPoint(plane_width, plane_height, 0, lc, 3)
    gmsh.model.geo.addPoint(0, plane_height, 0, lc, 4)
    gmsh.model.geo.addLine(1, 2, 1)
    gmsh.model.geo.addLine(3, 2, 2)
    gmsh.model.geo.addLine(3, 4, 3)
    gmsh.model.geo.addLine(4, 1, 4)
    gmsh.model.geo.addCurveLoop([1, -2, 3, 4], 1)

    # Initialize list to store curve loops for the main surface and pores
    curve_loops = [1]

    # Define pores as circles and create curve loops for each
    for i in range(len(pores)):
        pore = pores[i]

        left = gmsh.model.geo.addPoint(pore.x - pore.r, pore.y, 0, cm)
        center = gmsh.model.geo.addPoint(pore.x, pore.y, 0, cm)
        right = gmsh.model.geo.addPoint(pore.x + pore.r, pore.y, 0, cm)

        gmsh.model.geo.addCircleArc(left, center, right, 5+2*i)
        gmsh.model.geo.addCircleArc(right, center, left, 6+2*i)
        gmsh.model.geo.addCurveLoop([5+2*i, 6+2*i], 2+i)

        # Append each pore curve loop to the curve_loops list
        curve_loops.append(2+i)

    surface_tag = gmsh.model.geo.addPlaneSurface(curve_loops)
    gmsh.model.geo.synchronize()

    gmsh.model.mesh.generate(2)
    gmsh.write(f"../SavedMeshFiles/mesh{m + 1}.msh")
    XdmfConverter.convert_msh_to_xdmf(f"../SavedMeshFiles/mesh{m + 1}.msh", f"../SavedXdmfFiles/mesh{m + 1}.xdmf")

    # Clear the current model to free up memory for the next one
    gmsh.clear()

gmsh.finalize()