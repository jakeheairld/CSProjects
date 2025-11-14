from sympy import Eq, solve
from sympy.abc import a, b, c, d, e, t, k
import matplotlib.pyplot as plt
import numpy as np

f = a * t ** 4 + b * t ** 3 + c * t ** 2 + d * t + e
eq1 = Eq(f.subs(t, 0), k)
eq2 = Eq(f.subs(t, 1), 1)
eq3 = Eq(f.subs(t, 2), 1)
eq4 = Eq(f.diff(t).subs(t, 0), 0)
eq5 = Eq(f.diff(t).subs(t, 2), 0)
sol = solve([eq1, eq2, eq3, eq4, eq5], (a, b, c, d, e))

# k + (2 * t ** 2 - 9 * t + 11) * t ** 2 * (1 - k) / 4

theta = np.linspace(0, 2 * np.pi)
k = 0.8
theta1 = 80 * np.pi / 180  # a deformation at theta 80 degrees
alpha = 36 * np.pi / 180  # have a special point every 36 degrees (10 on the circle)
th = theta - theta1  # the difference between the angles, still needs to be careful to make this difference symmetrical to zero
t = np.abs(np.where(th < np.pi, th, th - 2 * np.pi)) / alpha  # use absolute value and let alpha represent a step of 1
r = np.where(t > 2, 1, k + (2 * t ** 2 - 9 * t + 11) * t ** 2 * (1 - k) / 4) # the deformed radius

plt.plot(np.cos(theta), np.sin(theta), ':r')
plt.plot(r * np.cos(theta), r * np.sin(theta), '-b')
plt.fill(r * np.cos(theta), r * np.sin(theta), color='blue', alpha=0.2)
for i in range(-5, 5):
    plt.plot(np.cos(theta1 + i * alpha), np.sin(theta1 + i * alpha), 'xk')
plt.axis('equal')
plt.show()

