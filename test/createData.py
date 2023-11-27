import pandas as pd
import numpy as np


status_values = [0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,999]
temp_values = np.concatenate((np.round(np.random.uniform(0, 120, size=100), 2), [999]))
percent_values = np.concatenate((np.round(np.random.uniform(0, 100, size=100), 2), [999]))

in_values = np.concatenate((np.round(np.random.uniform(10, 50, size=100), 2), [999]))
out_values = np.concatenate((np.round(np.random.uniform(10, 60, size=100), 2), [999]))
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]




# Create a sample dataset
n_samples = 30000
n_features = 8
X = np.random.choice(status_values, size=n_samples)
ivo_in_temp = np.random.choice(in_values, size=n_samples)
ivo_in_temp_status = np.random.choice(status_values, size=n_samples)
ivo_out_temp = np.random.choice(out_values, size=n_samples)
ivo_out_temp_status = np.random.choice(status_values, size=n_samples)
npe_in_temp = np.random.choice(in_values, size=n_samples)
npe_in_temp_status = np.random.choice(status_values, size=n_samples)
npe_out_temp = np.random.choice(out_values, size=n_samples)
npe_out_temp_status = np.random.choice(status_values, size=n_samples)
cpu_use = np.random.choice(percent_values, size=n_samples)
c_space_use = np.random.choice(percent_values, size=n_samples)
mem_use = np.random.choice(percent_values, size=n_samples)
root_space_use = np.random.choice(percent_values, size=n_samples)
available_mem = np.random.choice(percent_values, size=n_samples)
sda_disk_use = np.random.choice(percent_values, size=n_samples)


trigger = np.random.choice(trigger_values, size=n_samples)


# Create a dictionary of column names and their corresponding arrays
# ivo is I/O
cols = {
    'psu_status': X,
    'ivo_in_temp': ivo_in_temp,
    'ivo_in_temp_status': ivo_in_temp_status,
    'ivo_out_temp': ivo_out_temp,
    'ivo_out_temp_status': ivo_out_temp_status,
    'npe_in_temp': npe_in_temp,
    'npe_in_temp_status': npe_in_temp_status,
    'npe_out_temp': npe_out_temp,
    'npe_out_temp_status': npe_out_temp_status,
    'cpu_use': cpu_use,
    'c_space_use': c_space_use,
    'mem_use': mem_use,
    'root_space_use': root_space_use,
    'available_mem': available_mem,
    'sda_disk_use': sda_disk_use,
    'trigger': trigger
}


### 999 is none
    
def set_random_value(row, status_col, value_col, fvalues):
    if row[status_col] == 0:
        new_value = np.random.choice(fvalues)
        return new_value
    else:
        return row[value_col]
    
    
def set_none(row, col1, col2):
    if row[col1] == 999 or row[col2] == 999:
        return 999
    else:
        return row[col1]
    



in_high = np.round(np.random.uniform(41, 55, size=100), 2)
out_high = np.round(np.random.uniform(51, 60, size=100), 2)

df = pd.DataFrame(cols)
df['ivo_in_temp'] = df.apply(lambda row: set_random_value(row, 'ivo_in_temp_status', 'ivo_in_temp', in_high), axis=1)
df['ivo_out_temp'] = df.apply(lambda row: set_random_value(row, 'ivo_out_temp_status', 'ivo_out_temp', out_high), axis=1)
df['npe_in_temp'] = df.apply(lambda row: set_random_value(row, 'npe_in_temp_status', 'npe_in_temp', in_high), axis=1)
df['npe_out_temp'] = df.apply(lambda row: set_random_value(row, 'npe_out_temp_status', 'npe_out_temp', out_high), axis=1)


df['ivo_in_temp'] = df.apply(lambda row: set_none(row, 'ivo_in_temp', 'ivo_in_temp_status'), axis=1)
df['ivo_in_temp_status'] = df.apply(lambda row: set_none(row, 'ivo_in_temp_status', 'ivo_in_temp'), axis=1)
df['ivo_out_temp'] = df.apply(lambda row: set_none(row, 'ivo_out_temp', 'ivo_out_temp_status'), axis=1)
df['ivo_out_temp_status'] = df.apply(lambda row: set_none(row, 'ivo_out_temp_status', 'ivo_out_temp'), axis=1)
df['npe_in_temp'] = df.apply(lambda row: set_none(row, 'npe_in_temp', 'npe_in_temp_status'), axis=1)
df['npe_in_temp_status'] = df.apply(lambda row: set_none(row, 'npe_in_temp_status', 'npe_in_temp'), axis=1)
df['npe_out_temp'] = df.apply(lambda row: set_none(row, 'npe_out_temp', 'npe_out_temp_status'), axis=1)
df['npe_out_temp_status'] = df.apply(lambda row: set_none(row, 'npe_out_temp_status', 'npe_out_temp'), axis=1)

df['ivo_in_temp'] = df.apply(lambda row: set_none(row, 'ivo_in_temp', 'ivo_out_temp'), axis=1)
df['ivo_out_temp'] = df.apply(lambda row: set_none(row, 'ivo_out_temp', 'ivo_in_temp'), axis=1)
df['npe_in_temp'] = df.apply(lambda row: set_none(row, 'npe_in_temp', 'npe_out_temp'), axis=1)
df['npe_out_temp'] = df.apply(lambda row: set_none(row, 'npe_out_temp', 'npe_in_temp'), axis=1)
df['ivo_in_temp_status'] = df.apply(lambda row: set_none(row, 'ivo_in_temp_status', 'ivo_out_temp_status'), axis=1)
df['ivo_out_temp_status'] = df.apply(lambda row: set_none(row, 'ivo_out_temp_status', 'ivo_in_temp_status'), axis=1)
df['npe_in_temp_status'] = df.apply(lambda row: set_none(row, 'npe_in_temp_status', 'npe_out_temp_status'), axis=1)
df['npe_out_temp_status'] = df.apply(lambda row: set_none(row, 'npe_out_temp_status', 'npe_in_temp_status'), axis=1)

def update_status(row, value_col, status_col, threshold):
    if isinstance(row[value_col], int) and row[value_col] > threshold:
        return 0
    else:
        return row[status_col]


df['ivo_in_temp_status'] = df.apply(lambda row: update_status(row, 'ivo_in_temp', 'ivo_in_temp_status', 40), axis=1)
df['ivo_out_temp_status'] = df.apply(lambda row: update_status(row, 'ivo_out_temp', 'ivo_out_temp_status', 50), axis=1)
df['npe_in_temp_status'] = df.apply(lambda row: update_status(row, 'npe_in_temp', 'npe_in_temp_status', 40), axis=1)
df['npe_out_temp_status'] = df.apply(lambda row: update_status(row, 'npe_out_temp', 'npe_out_temp_status', 50), axis=1)




# Define a function to calculate the probability of device failure
def calculate_failure_prob(row):

    psu_status = row['psu_status']
    ivo_in_temp = row['ivo_in_temp']
    ivo_in_temp_status = row['ivo_in_temp_status']
    ivo_out_temp = row['ivo_out_temp']
    ivo_out_temp_status = row['ivo_out_temp_status']
    npe_in_temp = row['npe_in_temp']
    npe_in_temp_status = row['npe_in_temp_status']
    npe_out_temp = row['npe_out_temp']
    npe_out_temp_status = row['npe_out_temp_status']
    cpu_use = row['cpu_use']
    c_space_use = row['c_space_use']
    mem_use = row['mem_use']
    root_space_use = row['root_space_use']
    available_mem = row['available_mem']
    sda_disk_use = row['sda_disk_use']

    trigger = row['trigger']
    prob = 0


    statuses = [ psu_status, ivo_in_temp_status, ivo_out_temp_status, npe_in_temp_status, npe_out_temp_status]
    
    if any(status == 0 for status in statuses):
        prob += 1

    if ivo_in_temp > 40 and ivo_in_temp != 999:
        prob += 1
    if ivo_out_temp > 50 and ivo_out_temp != 999:
        prob += 1
    if npe_in_temp > 40 and npe_in_temp != 999:
        prob += 1
    if npe_out_temp > 50 and npe_out_temp != 999:
        prob += 1
    if cpu_use == 100 and cpu_use != 999:
        prob += 1
    if c_space_use == 100 and c_space_use != 999:
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
df.to_csv('device_performance.csv', index=False)
print("done")
