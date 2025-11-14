import random
import math
import matplotlib.pyplot as plt
import matplotlib.patches as patches


# Base class for pore shapes
class Pore:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def plot(self, ax):
        pass


# Circle class
class Circle(Pore):
    def __init__(self, x, y, r):
        super().__init__(x, y)
        self.r = r

    def plot(self, ax):
        circle_patch = plt.Circle((self.x, self.y), self.r, edgecolor='b', facecolor='none')
        ax.add_patch(circle_patch)


# Square class
class Square(Pore):
    def __init__(self, x, y, side_length):
        super().__init__(x, y)
        self.side_length = side_length

    def plot(self, ax):
        square_patch = patches.Rectangle((self.x - self.side_length / 2, self.y - self.side_length / 2),
                                         self.side_length, self.side_length, linewidth=1, edgecolor='g', facecolor='none')
        ax.add_patch(square_patch)


# Ellipse class
class Ellipse(Pore):
    def __init__(self, x, y, width, height):
        super().__init__(x, y)
        self.width = width
        self.height = height

    def plot(self, ax):
        ellipse_patch = patches.Ellipse((self.x, self.y), self.width, self.height, linewidth=1, edgecolor='r', facecolor='none')
        ax.add_patch(ellipse_patch)


# Function to generate random pores with random shapes
def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []

    while len(pores) < n_pores:
        # Randomly choose shape type (circle, square, or ellipse)
        shape_type = random.choice(["circle", "square", "ellipse"])

        if shape_type == "circle":
            radius = random.uniform(r_min, r_max)
            x = random.uniform(radius, w_width - radius)
            y = random.uniform(radius, w_height - radius)
            pores.append(Circle(x, y, radius))

        elif shape_type == "square":
            side_length = random.uniform(r_min, r_max)
            x = random.uniform(side_length / 2, w_width - side_length / 2)
            y = random.uniform(side_length / 2, w_height - side_length / 2)
            pores.append(Square(x, y, side_length))

        elif shape_type == "ellipse":
            width = random.uniform(r_min, r_max)
            height = random.uniform(r_min, r_max)
            x = random.uniform(width / 2, w_width - width / 2)
            y = random.uniform(height / 2, w_height - height / 2)
            pores.append(Ellipse(x, y, width, height))

    return pores


# Visualize the pores using matplotlib
def plot_pores(pores_to_generate, area_width, area_height):
    fig, ax = plt.subplots()
    ax.set_xlim((0, area_width))
    ax.set_ylim((0, area_height))

    for pore in pores_to_generate:
        pore.plot(ax)

    plt.gca().set_aspect('equal', adjustable='box')
    plt.show()


# Parameters
num_pores = 20
radius_min = 5
radius_max = 20
window_width = 100
window_height = 100

# Generate and plot pores
pores = generate_pores(num_pores, radius_min, radius_max, window_width, window_height)
plot_pores(pores, window_width, window_height)
