import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import numpy as np

# Load the data from the CSV file
df = pd.read_csv('testwindow.csv')

# Split the data into input features (X) and target labels (y)
X = df[['cpu_use', 'c_space_use', 'mem_use', 'cpu_temp', 'gpu_temp', 'trigger']]
y = df['failure_chance']

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Create a Logistic Regression model
model = LogisticRegression()

# Train the model
model.fit(X_train, y_train)

# Make predictions on the test data
y_pred = model.predict(X_test)

# Calculate accuracy
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy:.2f}')

# Generate some new test data
n_samples = 10

percent_values = np.round(np.random.uniform(0, 100, size=n_samples), 2)
temp_values = np.round(np.random.uniform(1, 110, size=n_samples), 2)
trigger_values =[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4,5]
trigger_test = np.random.choice(trigger_values, size=n_samples)

# Create a DataFrame for the test data
test_data = pd.DataFrame({
    'cpu_use': percent_values,
    'c_space_use': percent_values,
    'mem_use': percent_values,
    'cpu_temp': temp_values,
    'gpu_temp': temp_values,
    'trigger': trigger_test
})

# Make predictions on the test data
y_pred = model.predict(test_data)
y_pred_pct = np.round(y_pred * 100, decimals=2)

# Print the results
for i in range(n_samples):
    print("*******************************************")
    print(f"Test Data {i + 1}: Failure chance: {y_pred_pct[i][0]}")
