import numpy as np

# Generate 10 random numbers with zero mean and unit variance
n_samples = 10
random_numbers = np.random.normal(loc=0, scale=1, size=n_samples)

print(random_numbers)