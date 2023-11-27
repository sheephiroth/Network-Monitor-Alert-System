import pandas as pd
import tensorflow as tf
import numpy as np


# Load the data from the CSV file
df = pd.read_csv('Window.csv')


# Split the data into input features (X) and target labels (y)
X = df[[ 'cpu_use','c_space_use','mem_use','cpu_temp','gpu_temp','trigger']].values
y = df['failure_chance'].values

# Define the input variables 
cpu_use = tf.keras.Input(shape=(1,))
c_space_use = tf.keras.Input(shape=(1,))
mem_use = tf.keras.Input(shape=(1,))
cpu_temp = tf.keras.Input(shape=(1,))
gpu_temp = tf.keras.Input(shape=(1,))

trigger = tf.keras.Input(shape=(1,))


# Combine the input variables into a single tensor
input_tensor = tf.keras.layers.concatenate([cpu_use,c_space_use,mem_use,cpu_temp
           ,gpu_temp,trigger])



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
model = tf.keras.Model(inputs=[cpu_use,c_space_use,mem_use,cpu_temp
           ,gpu_temp,trigger], outputs=output_tensor)
# Define the new optimizer
opt = tf.keras.optimizers.Adam(learning_rate=0.001)


# Compile the model with the new optimizer
model.compile(optimizer=opt, loss='binary_crossentropy', metrics=['accuracy'])

# Train the model with the new optimizer
model.fit(x=[X[:,0], X[:,1], X[:,2], X[:,3], X[:,4], X[:,5]], y=y,
           epochs=1000, batch_size=128)

# Generate some new test data
n_samples = 10

percent_values = np.concatenate((np.round(np.random.uniform(0, 100, size=100), 2), [999]))
cpu_temp_values = np.concatenate((np.round(np.random.uniform(1, 110, size=100), 2), [999]))
gpu_temp_values = np.concatenate((np.round(np.random.uniform(1, 105, size=100), 2), [999]))
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]



cpu_use_test = np.random.choice(percent_values, size=n_samples)
c_space_use_test = np.random.choice(percent_values, size=n_samples)
mem_use_test = np.random.choice(percent_values, size=n_samples)
cpu_temp_test = np.random.choice(cpu_temp_values, size=n_samples)
gpu_temp_test = np.random.choice(gpu_temp_values, size=n_samples)

trigger_test = np.random.choice(trigger_values, size=n_samples)





    



# Make predictions on the test data
y_pred = model.predict([cpu_use_test,c_space_use_test,mem_use_test,cpu_temp_test,gpu_temp_test,trigger_test])
y_pred_pct = np.round(y_pred * 100, decimals=2)


# Print the results
for i in range(n_samples):
    print("******************************************* \n CPU Use {:.2f}%, C/ space use {:.2f} %, Memmory use {:.2f} % \n CPU temp : {:.2f} °C, GPU temp : {:.2f} °C,   \n Triggers => {:} \n Failure chance: {:.2f}% \n"
          .format("none" if cpu_use_test[i] == 999.9 else cpu_use_test[i], 
                  "none" if c_space_use_test[i] == 999.9 else c_space_use_test[i], 
                  "none" if mem_use_test[i] == 999.9 else mem_use_test[i], 
                  "none" if cpu_temp_test[i] == 999.9 else cpu_temp_test[i], 
                  "none" if gpu_temp_test[i] == 999.9 else gpu_temp_test[i], 
                  "none" if trigger_test[i] == 999.9 else trigger_test[i], 
                  y_pred_pct[i][0]))

model.save('Window.h5')
