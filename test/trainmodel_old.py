import pandas as pd
import tensorflow as tf
import numpy as np


# Load the data from the CSV file
df = pd.read_csv('device_performance.csv')

# Split the data into input features (X) and target labels (y)
X = df[['cpu_temp', 'cpu_temp_status','gpu_temp', 'gpu_temp_status','ram_temp', 'ram_temp_status', 'psu_status',
         'ivo_in_temp', 'ivo_in_temp_status','ivo_out_temp', 'ivo_out_temp_status','npe_in_temp',
         'npe_in_temp_status','npe_out_temp', 'npe_out_temp_status','trigger']].values
y = df['failure_chance'].values

# Define the input variables 
cpu_temp = tf.keras.Input(shape=(1,))
cpu_temp_status = tf.keras.Input(shape=(1,))
gpu_temp = tf.keras.Input(shape=(1,))
gpu_temp_status = tf.keras.Input(shape=(1,))
ram_temp = tf.keras.Input(shape=(1,))
ram_temp_status = tf.keras.Input(shape=(1,))
psu_status = tf.keras.Input(shape=(1,))
ivo_in_temp = tf.keras.Input(shape=(1,))
ivo_in_temp_status = tf.keras.Input(shape=(1,))
ivo_out_temp = tf.keras.Input(shape=(1,))
ivo_out_temp_status = tf.keras.Input(shape=(1,))
npe_in_temp = tf.keras.Input(shape=(1,))
npe_in_temp_status = tf.keras.Input(shape=(1,))
npe_out_temp = tf.keras.Input(shape=(1,))
npe_out_temp_status = tf.keras.Input(shape=(1,))
trigger = tf.keras.Input(shape=(1,))

# Combine the input variables into a single tensor
input_tensor = tf.keras.layers.concatenate([cpu_temp, cpu_temp_status, gpu_temp, gpu_temp_status, ram_temp, ram_temp_status, psu_status,
         ivo_in_temp, ivo_in_temp_status, ivo_out_temp, ivo_out_temp_status, npe_in_temp, npe_in_temp_status,
         npe_out_temp, npe_out_temp_status,trigger])

def adaptive_l2_reg(factor):
    def regularizer(weights):
        scale = factor / (tf.reduce_sum(tf.square(weights)) + 1e-12)
        return scale * tf.reduce_sum(tf.square(weights))
    return regularizer

# Define the neural network architecture with AdaLA2 regularization and dropout

x = tf.keras.layers.Dense(256, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(input_tensor)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(128, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(64, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(32, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(16, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(x)
x = tf.keras.layers.Dropout(0.1)(x)
x = tf.keras.layers.Dense(8, activation='relu', kernel_regularizer=adaptive_l2_reg(0.001))(x)
output_tensor = tf.keras.layers.Dense(1, activation='sigmoid')(x)

# Create the model
model = tf.keras.Model(inputs=[cpu_temp, cpu_temp_status, gpu_temp, gpu_temp_status, ram_temp, ram_temp_status, psu_status,
         ivo_in_temp, ivo_in_temp_status, ivo_out_temp, ivo_out_temp_status, npe_in_temp, npe_in_temp_status,
         npe_out_temp, npe_out_temp_status,trigger], outputs=output_tensor)
# Define the new optimizer
opt = tf.keras.optimizers.Adam(learning_rate=0.001)


# Compile the model with the new optimizer
model.compile(optimizer=opt, loss='binary_crossentropy', metrics=['accuracy'])

# Train the model with the new optimizer
model.fit(x=[X[:,0], X[:,1], X[:,2], X[:,3], X[:,4], X[:,5], X[:,6], X[:,7], X[:,8], X[:,9], X[:,10], X[:,11], X[:,12], X[:,13], X[:,14], X[:,15]], y=y,
           epochs=100, batch_size=64)

# Generate some new test data
num_test_samples = 10

status_values = [0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,999]
cpu_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,
  97,98,99,100,101,102,103,104,105,106,107,108,109,110,999]
gpu_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,
  97,98,99,100,101,102,103,104,105,999]
ram_values =[10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,999]
in_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,999]
out_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,999]
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]



cpu_temp_test = np.random.choice(cpu_values, size=(num_test_samples, 1))
cpu_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
gpu_temp_test = np.random.choice(gpu_values, size=(num_test_samples, 1))
gpu_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
ram_temp_test = np.random.choice(ram_values, size=(num_test_samples, 1))
ram_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
psu_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
ivo_in_temp_test = np.random.choice(in_values, size=(num_test_samples, 1))
ivo_in_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
ivo_out_temp_test = np.random.choice(out_values, size=(num_test_samples, 1))
ivo_out_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
npe_in_temp_test = np.random.choice(in_values, size=(num_test_samples, 1))
npe_in_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
npe_out_temp_test = np.random.choice(out_values, size=(num_test_samples, 1))
npe_out_temp_status_test = np.random.choice(status_values, size=(num_test_samples, 1))
trigger_test = np.random.choice(trigger_values, size=(num_test_samples, 1))

cpu_high = [101,102,103,104,105,106,107,108,109,110]
gpu_high = [96,97,98,99,100,101,102,103,104,105,106,107,108,109,110]
ram_high = [86,87,88,89,90,91,92,93,94,95,96,97,98,99,100]
in_high = [41,42,43,44,45,46,47,48,49,50,51,52,53,54,55]
out_high = [51,52,53,54,55,56,57,58,59,60]


for i in range(num_test_samples):
    if cpu_temp_status_test[i] == 0:
        cpu_temp_test[i] = np.random.choice(cpu_high, size=(1,))
    if gpu_temp_status_test[i] == 0:
        gpu_temp_test[i] = np.random.choice(gpu_high, size=(1,))
    if ram_temp_status_test[i] == 0:
        ram_temp_test[i] = np.random.choice(ram_high, size=(1,))
    if ivo_in_temp_status_test[i] == 0:
        ivo_in_temp_test[i] = np.random.choice(in_high, size=(1,))
    if ivo_out_temp_status_test[i] == 0:
        ivo_out_temp_test[i] = np.random.choice(out_high, size=(1,))
    if npe_in_temp_status_test[i] == 0:
        npe_in_temp_test[i] = np.random.choice(in_high, size=(1,))
    if npe_out_temp_status_test[i] == 0:
        npe_out_temp_test[i] = np.random.choice(out_high, size=(1,))
    if cpu_temp_test[i] > 100 and cpu_temp_test[i] != 999:
        cpu_temp_status_test[i] = 0
    if gpu_temp_test[i] > 95 and gpu_temp_test[i] != 999:
        gpu_temp_status_test[i] = 0
    if ram_temp_test[i] > 85 and ram_temp_test[i] != 999:
        ram_temp_status_test[i] = 0
    if ivo_in_temp_test[i] > 40 and ivo_in_temp_test[i] != 999:
        ivo_in_temp_status_test[i] = 0
    if ivo_out_temp_test[i] > 50 and ivo_out_temp_test[i] != 999:
        ivo_out_temp_status_test[i] = 0
    if npe_in_temp_test[i] > 40 and npe_in_temp_test[i] != 999:
        npe_in_temp_status_test[i] = 0
    if npe_out_temp_test[i] > 50 and npe_out_temp_test[i] != 999:
        npe_out_temp_status_test[i] = 0
    
    if cpu_temp_test[i] == 999 or cpu_temp_status_test[i] == 999:
        cpu_temp_test[i] = 999
        cpu_temp_status_test[i] = 999
    if gpu_temp_test[i] == 999 or gpu_temp_status_test[i] == 999:
        gpu_temp_test[i] = 999
        gpu_temp_status_test[i] = 999
    if ram_temp_test[i] == 999 or ram_temp_status_test[i] == 999:
        ram_temp_test[i] = 999
        ram_temp_status_test[i] = 999


    if ivo_in_temp_test[i] == 999 or ivo_in_temp_status_test[i] == 999 or ivo_out_temp_test[i] == 999 or ivo_out_temp_status_test[i] == 999:
        ivo_in_temp_test[i] = 999
        ivo_in_temp_status_test[i] = 999
        ivo_out_temp_test[i] = 999
        ivo_out_temp_status_test[i] = 999
    if npe_in_temp_test[i] == 999 or npe_in_temp_status_test[i] == 999 or npe_out_temp_test[i] == 999 or npe_out_temp_status_test[i] == 999:
        npe_in_temp_test[i] = 999
        npe_in_temp_status_test[i] = 999
        npe_out_temp_test[i] = 999
        npe_out_temp_status_test[i] = 999
    



# Make predictions on the test data
y_pred = model.predict([cpu_temp_test, cpu_temp_status_test, gpu_temp_test, gpu_temp_status_test, ram_temp_test, ram_temp_status_test, psu_status_test,
         ivo_in_temp_test, ivo_in_temp_status_test, ivo_out_temp_test, ivo_out_temp_status_test, npe_in_temp_test, npe_in_temp_status_test,
         npe_out_temp_test, npe_out_temp_status_test,trigger_test])
y_pred_pct = np.round(y_pred * 100, decimals=2)


# Print the results
for i in range(num_test_samples):
    print("******************************************* \n Name => Temp:status \n CPU => {:}°C:{:}, GPU => {:}°C:{:}, RAM => {:}°C:{:} I/O IN => {:}:{:}, I/O OUT => {:}:{:}, NPE IN => {:}:{:}, NPE OUT => {:}:{:} \n PSU status => {:} , Triggers => {:} \n Failure chance: {:.2f}% \n"
          .format("none" if cpu_temp_test[i][0] == 999 else cpu_temp_test[i][0], 
                  "none" if cpu_temp_status_test[i][0] == 999 else cpu_temp_status_test[i][0], 
                  "none" if gpu_temp_test[i][0] == 999 else gpu_temp_test[i][0], 
                  "none" if gpu_temp_status_test[i][0] == 999 else gpu_temp_status_test[i][0], 
                  "none" if ram_temp_test[i][0] == 999 else ram_temp_test[i][0], 
                  "none" if ram_temp_status_test[i][0] == 999 else ram_temp_status_test[i][0],
                  "none" if ivo_in_temp_test[i][0] == 999 else ivo_in_temp_test[i][0], 
                  "none" if ivo_in_temp_status_test[i][0] == 999 else ivo_in_temp_status_test[i][0], 
                  "none" if ivo_out_temp_test[i][0] == 999 else ivo_out_temp_test[i][0], 
                  "none" if ivo_out_temp_status_test[i][0] == 999 else ivo_out_temp_status_test[i][0], 
                  "none" if npe_in_temp_test[i][0] == 999 else npe_in_temp_test[i][0], 
                  "none" if npe_in_temp_status_test[i][0] == 999 else npe_in_temp_status_test[i][0],
                  "none" if npe_out_temp_test[i][0] == 999 else npe_out_temp_test[i][0], 
                  "none" if npe_out_temp_status_test[i][0] == 999 else npe_out_temp_status_test[i][0], 
                  "none" if psu_status_test[i][0] == 999 else psu_status_test[i][0], 
                  "none" if trigger_test[i][0] == 999 else trigger_test[i][0], 
                  y_pred_pct[i][0]))

# model.save('testmodel.h5')
