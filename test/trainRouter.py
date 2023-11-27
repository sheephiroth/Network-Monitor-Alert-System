import pandas as pd
import tensorflow as tf
import numpy as np


# Load the data from the CSV file
df = pd.read_csv('Router.csv')


# Split the data into input features (X) and target labels (y)
X = df[[ 'psu_status','ivo_in_temp','ivo_in_temp_status','ivo_out_temp','ivo_out_temp_status',
        'npe_in_temp','npe_in_temp_status','npe_out_temp','npe_out_temp_status','trigger']].values
y = df['failure_chance'].values

# Define the input variables 
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
input_tensor = tf.keras.layers.concatenate([ psu_status,ivo_in_temp, ivo_in_temp_status,
         ivo_out_temp, ivo_out_temp_status, npe_in_temp, npe_in_temp_status,npe_out_temp,
           npe_out_temp_status,trigger])



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
model = tf.keras.Model(inputs=[psu_status,ivo_in_temp, ivo_in_temp_status,
         ivo_out_temp, ivo_out_temp_status, npe_in_temp, npe_in_temp_status,npe_out_temp,
           npe_out_temp_status,trigger], outputs=output_tensor)
# Define the new optimizer
opt = tf.keras.optimizers.Adam(learning_rate=0.001)


# Compile the model with the new optimizer
model.compile(optimizer=opt, loss='binary_crossentropy', metrics=['accuracy'])

# Train the model with the new optimizer
model.fit(x=[X[:,0], X[:,1], X[:,2], X[:,3], X[:,4], X[:,5], X[:,6], X[:,7], X[:,8], X[:,9]], y=y,
           epochs=30000, batch_size=256)

# Generate some new test data
n_samples = 10

status_values = [0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,999.9]
in_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,999.9]
out_values = [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,
  49,50,51,52,53,54,55,56,57,58,59,60,999.9]
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]



psu_status_test = np.random.choice(status_values, size=n_samples)
ivo_in_temp_test = np.random.choice(in_values, size=n_samples)
ivo_in_temp_status_test = np.random.choice(status_values, size=n_samples)
ivo_out_temp_test = np.random.choice(out_values, size=n_samples)
ivo_out_temp_status_test = np.random.choice(status_values, size=n_samples)
npe_in_temp_test = np.random.choice(in_values, size=n_samples)
npe_in_temp_status_test = np.random.choice(status_values, size=n_samples)
npe_out_temp_test = np.random.choice(out_values, size=n_samples)
npe_out_temp_status_test = np.random.choice(status_values, size=n_samples)

trigger_test = np.random.choice(trigger_values, size=n_samples)

in_high = [41,42,43,44,45,46,47,48,49,50,51,52,53,54,55]
out_high = [51,52,53,54,55,56,57,58,59,60]


for i in range(n_samples):
    if ivo_in_temp_status_test[i] == 0:
        ivo_in_temp_test[i] = np.random.choice(in_high, size=(1,))
    if ivo_out_temp_status_test[i] == 0:
        ivo_out_temp_test[i] = np.random.choice(out_high, size=(1,))
    if npe_in_temp_status_test[i] == 0:
        npe_in_temp_test[i] = np.random.choice(in_high, size=(1,))
    if npe_out_temp_status_test[i] == 0:
        npe_out_temp_test[i] = np.random.choice(out_high, size=(1,))
    
    if ivo_in_temp_test[i] > 40 and ivo_in_temp_test[i] != 999.9:
        ivo_in_temp_status_test[i] = 0
    if ivo_out_temp_test[i] > 50 and ivo_out_temp_test[i] != 999.9:
        ivo_out_temp_status_test[i] = 0
    if npe_in_temp_test[i] > 40 and npe_in_temp_test[i] != 999.9:
        npe_in_temp_status_test[i] = 0
    if npe_out_temp_test[i] > 50 and npe_out_temp_test[i] != 999.9:
        npe_out_temp_status_test[i] = 0
    

    if ivo_in_temp_test[i] == 999.9 or ivo_in_temp_status_test[i] == 999.9 or ivo_out_temp_test[i] == 999.9 or ivo_out_temp_status_test[i] == 999.9:
        ivo_in_temp_test[i] = 999.9
        ivo_in_temp_status_test[i] = 999.9
        ivo_out_temp_test[i] = 999.9
        ivo_out_temp_status_test[i] = 999.9
    if npe_in_temp_test[i] == 999.9 or npe_in_temp_status_test[i] == 999.9 or npe_out_temp_test[i] == 999.9 or npe_out_temp_status_test[i] == 999.9:
        npe_in_temp_test[i] = 999.9
        npe_in_temp_status_test[i] = 999.9
        npe_out_temp_test[i] = 999.9
        npe_out_temp_status_test[i] = 999.9

    



# Make predictions on the test data
y_pred = model.predict([psu_status_test,ivo_in_temp_test, ivo_in_temp_status_test,
         ivo_out_temp_test, ivo_out_temp_status_test, npe_in_temp_test,
           npe_in_temp_status_test,npe_out_temp_test,npe_out_temp_status_test,
           trigger_test])
y_pred_pct = np.round(y_pred * 100, decimals=2)


# Print the results
for i in range(n_samples):
    print("******************************************* \n PSU status => {:} \n Name => Temp:status :: I/O IN => {:}:{:}, I/O OUT => {:}:{:}, NPE IN => {:}:{:}, NPE OUT => {:}:{:} \n Triggers => {:} \n Failure chance: {:.2f}% \n"
          .format("none" if psu_status_test[i] == 999.9 else psu_status_test[i], 
                  "none" if ivo_in_temp_test[i] == 999.9 else ivo_in_temp_test[i], 
                  "none" if ivo_in_temp_status_test[i] == 999.9 else ivo_in_temp_status_test[i], 
                  "none" if ivo_out_temp_test[i] == 999.9 else ivo_out_temp_test[i], 
                  "none" if ivo_out_temp_status_test[i] == 999.9 else ivo_out_temp_status_test[i], 
                  "none" if npe_in_temp_test[i] == 999.9 else npe_in_temp_test[i], 
                  "none" if npe_in_temp_status_test[i] == 999.9 else npe_in_temp_status_test[i],
                  "none" if npe_out_temp_test[i] == 999.9 else npe_out_temp_test[i], 
                  "none" if npe_out_temp_status_test[i] == 999.9 else npe_out_temp_status_test[i], 
                  "none" if trigger_test[i] == 999.9 else trigger_test[i], 
                  y_pred_pct[i][0]))

model.save('Router.h5')
