import pandas as pd
import numpy as np


percent_values = np.round(np.random.uniform(0, 100, size=100), 1)
temp_valus = np.round(np.random.uniform(1, 110, size=100), 1)
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]




# Create a sample dataset
n_samples = 30000
n_features = 8
X = np.random.choice(percent_values, size=n_samples)
c_space_use = np.random.choice(percent_values, size=n_samples)
mem_use = np.random.choice(percent_values, size=n_samples)
cpu_temp = np.random.choice(temp_valus, size=n_samples)
gpu_temp = np.random.choice(temp_valus, size=n_samples)

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


    if cpu_use >= 95 :
        prob += 0.05
    if cpu_use == 100 :
        prob += 0.05
    if c_space_use == 100 :
        prob += 0.1
    if mem_use >= 95 :
        prob += 0.05
    if mem_use == 100 :
        prob += 0.05
    if cpu_temp >= 90 :
        prob += 0.1
    if cpu_temp >= 100 :
        prob += 0.15
    if cpu_temp >= 105 :
        prob += 0.5
    if gpu_temp >= 85 :
        prob += 0.1
    if gpu_temp >= 95 :
        prob += 0.15
    if gpu_temp >= 100 :
        prob += 0.4

    if trigger > 0:
        prob += trigger * 0.25
    
    return min(prob, 1)  

#Create the dataframe and calculate the failure probability for each device
df['failure_chance'] = df.apply(calculate_failure_prob, axis=1)
df['failure_chance'] = df['failure_chance'].round(2)

# Save the dataframe as a CSV file
df.to_csv('testwindow.csv', index=False)
print("done")
