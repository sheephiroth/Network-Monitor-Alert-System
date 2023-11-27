import pandas as pd
import numpy as np
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import KFold
import tensorflow as tf

# Load the data from the CSV file
df = pd.read_csv('testwindow.csv')

# Split the data into input features (X) and target labels (y)
X = df[['cpu_use', 'c_space_use', 'mem_use', 'cpu_temp', 'gpu_temp', 'trigger']].values
y = df['failure_chance'].values

# Define a function to create the model
def create_model():
    # Define the input variables
    input_tensors = [
        tf.keras.Input(shape=(1,)),
        tf.keras.Input(shape=(1,)),
        tf.keras.Input(shape=(1,)),
        tf.keras.Input(shape=(1,)),
        tf.keras.Input(shape=(1,)),
        tf.keras.Input(shape=(1,))
    ]

    # Combine the input variables into a single tensor
    input_tensor = tf.keras.layers.concatenate(input_tensors)

    # Define the neural network architecture without AdaLA2 regularization and with dropout
    x = tf.keras.layers.Dense(64, activation='relu')(input_tensor)
    x = tf.keras.layers.Dropout(0.05)(x)
    x = tf.keras.layers.Dense(32, activation='relu')(x)
    x = tf.keras.layers.Dropout(0.05)(x)
    x = tf.keras.layers.Dense(16, activation='relu')(x)
    x = tf.keras.layers.Dropout(0.05)(x)

    output_tensor = tf.keras.layers.Dense(1, activation='sigmoid')(x)

    # Create the model
    model = tf.keras.Model(inputs=input_tensors, outputs=output_tensor)
    return model

# Create a KFold cross-validator
kf = KFold(n_splits=5, shuffle=True, random_state=42)

# Initialize a list to store the mean absolute errors
mae_scores = []

# Perform cross-validation
for train_indices, test_indices in kf.split(X):
    X_train, X_test = X[train_indices], X[test_indices]
    y_train, y_test = y[train_indices], y[test_indices]

    # Create and compile the model
    model = create_model()
    opt = tf.keras.optimizers.Adam(learning_rate=0.001)
    model.compile(optimizer=opt, loss='binary_crossentropy', metrics=['accuracy'])

    # Train the model
    model.fit([X_train[:,0], X_train[:,1], X_train[:,2], X_train[:,3], X_train[:,4], X_train[:,5]], y_train, epochs=500, batch_size=128, verbose=2)

    # Make predictions on the test data
    y_pred = model.predict([X_test[:,0], X_test[:,1], X_test[:,2], X_test[:,3], X_test[:,4], X_test[:,5]])

    # Calculate Mean Absolute Error
    mae = mean_absolute_error(y_test, y_pred)
    mae_scores.append(mae)

# Calculate and print the mean MAE
mean_mae = np.mean(mae_scores)
print(f'Mean Absolute Error (Cross-Validation): {mean_mae:.2f}')
