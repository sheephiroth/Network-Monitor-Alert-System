import pandas as pd
import tensorflow as tf
import numpy as np


# Load the data from the CSV file
df = pd.read_csv('ZabbixServer.csv')


# Split the data into input features (X) and target labels (y)
X = df[[ 'cpu_use','mem_use','root_space_use','available_mem','sda_disk_use','trigger']].values
y = df['failure_chance'].values

# Define the input variables 
cpu_use = tf.keras.Input(shape=(1,))
mem_use = tf.keras.Input(shape=(1,))
root_space_use = tf.keras.Input(shape=(1,))
available_mem = tf.keras.Input(shape=(1,))
sda_disk_use = tf.keras.Input(shape=(1,))

trigger = tf.keras.Input(shape=(1,))


# Combine the input variables into a single tensor
input_tensor = tf.keras.layers.concatenate([ cpu_use,mem_use,root_space_use
           ,available_mem,sda_disk_use,trigger])



# Define the neural network architecture without AdaLA2 regularization and with dropout

x = tf.keras.layers.Dense(256, activation='relu')(input_tensor)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(128, activation='relu')(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(64, activation='relu')(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(32, activation='relu')(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(16, activation='relu')(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(8, activation='relu')(x)
output_tensor = tf.keras.layers.Dense(1, activation='sigmoid')(x)

# Create the model
model = tf.keras.Model(inputs=[cpu_use,mem_use,root_space_use
           ,available_mem,sda_disk_use,trigger], outputs=output_tensor)
# Define the new optimizer
opt = tf.keras.optimizers.Adam(learning_rate=0.001)


# Compile the model with the new optimizer
model.compile(optimizer=opt, loss='binary_crossentropy', metrics=['accuracy'])

# Train the model with the new optimizer
model.fit(x=[X[:,0], X[:,1], X[:,2], X[:,3], X[:,4], X[:,5]], y=y,
           epochs=2000, batch_size=128)

# Generate some new test data
n_samples = 10

percent_values = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,
  97,98,99,100,999.9]

trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]




cpu_use_test = np.random.choice(percent_values, size=n_samples)
mem_use_test = np.random.choice(percent_values, size=n_samples)
root_space_use_test = np.random.choice(percent_values, size=n_samples)
available_mem_test = np.random.choice(percent_values, size=n_samples)
sda_disk_use_test = np.random.choice(percent_values, size=n_samples)

trigger_test = np.random.choice(trigger_values, size=n_samples)

in_high = [41,42,43,44,45,46,47,48,49,50,51,52,53,54,55]
out_high = [51,52,53,54,55,56,57,58,59,60]




    



# Make predictions on the test data
y_pred = model.predict([cpu_use_test,mem_use_test,root_space_use_test,available_mem_test,sda_disk_use_test,trigger_test])
y_pred_pct = np.round(y_pred * 100, decimals=2)


# Print the results
for i in range(n_samples):
    print("******************************************* \n CPU Use {:}%, Memmory use {:} %, / space use {:} %, Available memory use {:} %, Zabbix server disk use {:} %,   \n Triggers => {:} \n Failure chance: {:.2f}% \n"
          .format("none" if cpu_use_test[i] == 999.9 else cpu_use_test[i],  
                  "none" if mem_use_test[i] == 999.9 else mem_use_test[i], 
                  "none" if root_space_use_test[i] == 999.9 else root_space_use_test[i], 
                  "none" if available_mem_test[i] == 999.9 else available_mem_test[i], 
                  "none" if sda_disk_use_test[i] == 999.9 else sda_disk_use_test[i], 
                  "none" if trigger_test[i] == 999.9 else trigger_test[i], 
                  y_pred_pct[i][0]))

model.save('ZabbixServer.h5')
