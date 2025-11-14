import random
import math

minDist = 0.025

#Circle pore class
class Pore:
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r
        self.type = 'Circle'

    # Method to check if two circles overlap
    def overlaps(self, other_pore):
        distance = math.sqrt((self.x - other_pore.x) ** 2 + (self.y - other_pore.y) ** 2)
        return distance < (self.r + other_pore.r + minDist)

def generate_pores(n_pores, r_min, r_max, w_width, w_height):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius-minDist)
        y = random.uniform(radius + minDist, w_height - radius-minDist)

        # Create a new circle
        new_pore = Pore(x, y, radius)

        # Check for overlaps with existing pores
        overlap = False
        for pore in pores:
            if new_pore.overlaps(pore):
                overlap = True
                break

        # If no overlap, add the pore to the list
        if not overlap:
            pores.append(new_pore)
    return pores


# Square Pore Class
class Pore_Square:
    def __init__(self, x, y, l):
        self.x = x  # Center x-coordinate
        self.y = y  # Center y-coordinate
        self.l = l  # Side length
        self.type = 'Square'

    # Method to check if two squares overlap
    def overlaps(self, other_pore):
        # Calculate half the side length for simplicity
        half_side = self.l / 2
        other_half_side = other_pore.l / 2

        # Check for overlap using axis-aligned bounding box (AABB) logic
        return not (
            self.x + half_side + minDist < other_pore.x - other_half_side or
            self.x - half_side - minDist > other_pore.x + other_half_side or
            self.y + half_side + minDist < other_pore.y - other_half_side or
            self.y - half_side - minDist > other_pore.y + other_half_side
        )

# Function to generate square pores
def generate_pores_square(n_pores, side_length_min, side_length_max, w_width, w_height):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and side length
        side_length = random.uniform(side_length_min, side_length_max)
        x = random.uniform(side_length / 2 + minDist, w_width - side_length / 2 - minDist)
        y = random.uniform(side_length / 2 + minDist, w_height - side_length / 2 - minDist)

        # Create a new square pore
        new_pore = Pore_Square(x, y, side_length)

        # Check for overlaps with existing pores
        overlap = False
        for pore in pores:
            if new_pore.overlaps(pore):
                overlap = True
                break

        # If no overlap, add the pore to the list
        if not overlap:
            pores.append(new_pore)
    return pores


# Ellipse Pore Class
class Pore_Ellipse:
    def __init__(self, x, y, a, b):
        self.x = x  # Center x-coordinate
        self.y = y  # Center y-coordinate
        self.a = a  # Semi-major axis
        self.b = b  # Semi-minor axis
        self.type = 'Ellipse'

    # Method to check if two ellipses overlap
    def overlaps(self, other_pore):
        # Approximation using the distance between centers and sum of axes
        distance = math.sqrt((self.x - other_pore.x) ** 2 + (self.y - other_pore.y) ** 2)
        return distance < (self.a + other_pore.a + minDist) and distance < (self.b + other_pore.b + minDist)

# Ellipse pore generator
def generate_pores_ellipse(n_pores, a_min, a_max, b_min, b_max, w_width, w_height):
    pores = []
    while len(pores) < n_pores:
        a = random.uniform(a_min, a_max)
        b = random.uniform(b_min, b_max)
        x = random.uniform(a + minDist, w_width - a - minDist)
        y = random.uniform(b + minDist, w_height - b - minDist)
        new_pore = Pore_Ellipse(x, y, a, b)
        if all(not new_pore.overlaps(p) for p in pores):
            pores.append(new_pore)
    return pores