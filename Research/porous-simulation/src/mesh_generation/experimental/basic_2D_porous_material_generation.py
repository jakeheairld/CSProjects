import random
import math
import matplotlib.pyplot as plt

# Circle class to represent a pore
class Circle:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r

    # Method to check if two circles overlap
    def overlaps(self, other_circle):
        distance = math.sqrt((self.x - other_circle.x) ** 2 + (self.y - other_circle.y) ** 2)
        return distance < (self.r + other_circle.r)


# Function to generate random pores without overlap
# Returns a list of pores
def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []

    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius, w_width - radius)
        y = random.uniform(radius, w_height - radius)

        # Create a new circle
        new_circle = Circle(x, y, radius)

        # Check for overlaps with existing pores
        overlap = False
        for circle in pores:
            if new_circle.overlaps(circle):
                overlap = True
                break

        # If no overlap, add the circle to the list
        if not overlap:
            pores.append(new_circle)

    return pores


# Visualize the pores using matplotlib
def plot_pores(pores_to_generate, area_width, area_height):
    fig, ax = plt.subplots()
    ax.set_xlim((0, area_width))
    ax.set_ylim((0, area_height))

    for circle in pores_to_generate:
        circle_patch = plt.Circle((circle.x, circle.y), circle.r, edgecolor='b', facecolor='none')
        ax.add_patch(circle_patch)

    plt.gca().set_aspect('equal', adjustable='box')
    plt.show()



# Parameters
pore_type = "circle"
num_pores = 5
radius_min = 2
radius_max = 30
window_width = 100
window_height = 100

# Generate and plot pores
pores = generate_pores(num_pores, radius_min, radius_max, window_width, window_height)
plot_pores(pores, window_width, window_height)