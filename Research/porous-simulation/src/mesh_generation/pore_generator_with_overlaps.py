import random
import math
import matplotlib.pyplot as plt
import numpy as np

minDist = 0.025

# Circle Pore Class
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r

    # Method to plot the pore (circle)
    def plot(self):
        # Create a circle
        circle = plt.Circle((self.x, self.y), self.r, color='b', fill=False)
        return circle

    # Method to check if two circles overlap
    def overlaps(self, other_pore):
        distance = math.sqrt((self.x - other_pore.x) ** 2 + (self.y - other_pore.y) ** 2)
        return distance < (self.r + other_pore.r + minDist)

# Function to generate circular pores
def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []
    fig, ax = plt.subplots()

    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius - minDist)
        y = random.uniform(radius + minDist, w_height - radius - minDist)

        # Create a new circle pore
        new_pore = Pore(x, y, radius)

        # Add the pore to the list without checking for overlaps
        pores.append(new_pore)

        # Plot the pore
        ax.add_artist(new_pore.plot())

    ax.set_xlim(0, w_width)
    ax.set_ylim(0, w_height)
    ax.set_aspect('equal', 'box')
    plt.show()

    return pores


# Square Pore Class
class Pore_Square:
    def __init__(self, x, y, l):
        self.x = x  # Center x-coordinate
        self.y = y  # Center y-coordinate
        self.l = l  # Side length

    # Method to plot the pore (square)
    def plot(self):
        half_side = self.l / 2
        square = plt.Rectangle((self.x - half_side, self.y - half_side), self.l, self.l, color='g', fill=False)
        return square

# Function to generate square pores
def generate_pores_square(n_pores, side_length_min, side_length_max, w_width, w_height):
    pores = []
    fig, ax = plt.subplots()

    while len(pores) < n_pores:
        # Generate random position and side length
        side_length = random.uniform(side_length_min, side_length_max)
        x = random.uniform(side_length / 2 + minDist, w_width - side_length / 2 - minDist)
        y = random.uniform(side_length / 2 + minDist, w_height - side_length / 2 - minDist)

        # Create a new square pore
        new_pore = Pore_Square(x, y, side_length)

        # Add the pore to the list without checking for overlaps
        pores.append(new_pore)

        # Plot the pore
        ax.add_artist(new_pore.plot())

    ax.set_xlim(0, w_width)
    ax.set_ylim(0, w_height)
    ax.set_aspect('equal', 'box')
    plt.show()

    return pores


# Ellipse Pore Class
class Pore_Ellipse:
    def __init__(self, x, y, a, b):
        self.x = x  # Center x-coordinate
        self.y = y  # Center y-coordinate
        self.a = a  # Semi-major axis
        self.b = b  # Semi-minor axis

    # Method to plot the pore (ellipse)
    def plot(self):
        ellipse = plt.Ellipse((self.x, self.y), self.a * 2, self.b * 2, edgecolor='r', facecolor='none')
        return ellipse

# Ellipse pore generator
def generate_pores_ellipse(n_pores, a_min, a_max, b_min, b_max, w_width, w_height):
    pores = []
    fig, ax = plt.subplots()

    while len(pores) < n_pores:
        a = random.uniform(a_min, a_max)
        b = random.uniform(b_min, b_max)
        x = random.uniform(a + minDist, w_width - a - minDist)
        y = random.uniform(b + minDist, w_height - b - minDist)
        new_pore = Pore_Ellipse(x, y, a, b)

        # Add the pore to the list without checking for overlaps
        pores.append(new_pore)

        # Plot the pore
        ax.add_artist(new_pore.plot())

    ax.set_xlim(0, w_width)
    ax.set_ylim(0, w_height)
    ax.set_aspect('equal', 'box')
    plt.show()

    return pores