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

def generate_pores(w_width, w_height, n_pores, r_min, r_max):
    pores = []
    while len(pores) < n_pores:
        # Generate random position and radius
        radius = random.uniform(r_min, r_max)
        x = random.uniform(radius + minDist, w_width - radius-minDist)
        y = random.uniform(radius + minDist, w_height - radius-minDist)

        # Create a new circle
        new_pore = Pore(x, y, radius)

        # If no overlap, add the pore to the list
        pores.append(new_pore)

    return pores