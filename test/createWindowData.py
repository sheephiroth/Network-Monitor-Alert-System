import pandas as pd
import numpy as np


percent_values = np.concatenate((np.round(np.random.uniform(0, 100, size=100), 2), [999]))
cpu_temp_values = np.concatenate((np.round(np.random.uniform(1, 110, size=100), 2), [999]))
gpu_temp_values = np.concatenate((np.round(np.random.uniform(1, 105, size=100), 2), [999]))
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]




# Create a sample dataset
n_samples = 30000
n_features = 8
X = np.random.choice(percent_values, size=n_samples)
c_space_use = np.random.choice(percent_values, size=n_samples)
mem_use = np.random.choice(percent_values, size=n_samples)
cpu_temp = np.random.choice(cpu_temp_values, size=n_samples)
gpu_temp = np.random.choice(gpu_temp_values, size=n_samples)

trigger = np.random.choice(trigger_values, size=n_samples)


# Create a dictionary of column names and their corresponding arrays
# ivo is I/O
cols = {
    'cpu_use': X,
    'c_space_use': c_space_use,
    'mem_use': mem_use,
    'cpu_temp': cpu_temp,
    "gpu_temp": gpu_temp,
    'trigger': trigger
}


### 999 is none
    
df = pd.DataFrame(cols)




# Define a function to calculate the probability of device failure
def calculate_failure_prob(row):

    cpu_use = row['cpu_use']
    c_space_use = row['c_space_use']
    mem_use = row['mem_use']
    cpu_temp = row['cpu_temp']
    gpu_temp = row['gpu_temp']

    trigger = row['trigger']
    prob = 0

    if cpu_use == 100 and cpu_use != 999:
        prob += 1
    if c_space_use == 100 and c_space_use != 999:
        prob += 1
    if mem_use == 100 and mem_use != 999:
        prob += 1
    if cpu_temp >= 100 and cpu_temp != 999:
        prob += 1
    if gpu_temp >= 95 and gpu_temp != 999:
        prob += 1

    if trigger > 0:
        prob += 1
    
    return min(prob, 1)  

#Create the dataframe and calculate the failure probability for each device
df['failure_chance'] = df.apply(calculate_failure_prob, axis=1)
df['failure_chance'] = df['failure_chance'].round(2)

# Save the dataframe as a CSV file
df.to_csv('Window.csv', index=False)
print("done")
