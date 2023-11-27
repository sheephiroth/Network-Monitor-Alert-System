import pandas as pd
import numpy as np




percent_values = np.concatenate((np.round(np.random.uniform(0, 100, size=100), 2), [999]))

in_values = np.concatenate((np.round(np.random.uniform(10, 50, size=100), 2), [999]))
out_values = np.concatenate((np.round(np.random.uniform(10, 60, size=100), 2), [999]))
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]




# Create a sample dataset
n_samples = 30000
n_features = 8
X = np.random.choice(percent_values, size=n_samples)
mem_use = np.random.choice(percent_values, size=n_samples)
root_space_use = np.random.choice(percent_values, size=n_samples)
available_mem = np.random.choice(percent_values, size=n_samples)
sda_disk_use = np.random.choice(percent_values, size=n_samples)


trigger = np.random.choice(trigger_values, size=n_samples)


# Create a dictionary of column names and their corresponding arrays
# ivo is I/O
cols = {
    'cpu_use': X,
    'mem_use': mem_use,
    'root_space_use': root_space_use,
    'available_mem': available_mem,
    'sda_disk_use': sda_disk_use,
    'trigger': trigger
}


### 999 is none
    

df = pd.DataFrame(cols)




# Define a function to calculate the probability of device failure
def calculate_failure_prob(row):

    cpu_use = row['cpu_use']
    mem_use = row['mem_use']
    root_space_use = row['root_space_use']
    available_mem = row['available_mem']
    sda_disk_use = row['sda_disk_use']

    trigger = row['trigger']
    prob = 0



    

    if cpu_use == 100 and cpu_use != 999:
        prob += 1
    if mem_use == 100 and mem_use != 999:
        prob += 1
    if root_space_use == 100 and root_space_use != 999:
        prob += 1
    if available_mem == 0 and available_mem != 999:
        prob += 1
    if sda_disk_use == 100 and sda_disk_use != 999:
        prob += 1

    if trigger > 0:
        prob += 1
    
    return min(prob, 1)  

#Create the dataframe and calculate the failure probability for each device
df['failure_chance'] = df.apply(calculate_failure_prob, axis=1)
df['failure_chance'] = df['failure_chance'].round(2)

# Save the dataframe as a CSV file
df.to_csv('ZabbixServer.csv', index=False)
print("done")
