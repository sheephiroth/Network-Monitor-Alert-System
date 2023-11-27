import tensorflow as tf

# Path to the input Keras model in .h5 format
keras_model_path = 'Router.h5'
# keras_model_path = 'ZabbixServer.h5'
# keras_model_path = 'Window.h5'


# Path to the output TensorFlow Lite model in .tflite format
tflite_model_path = 'Router.tflite'
# tflite_model_path = 'ZabbixServer.tflite'
# tflite_model_path = 'Window.tflite'


# Load the Keras model
keras_model = tf.keras.models.load_model(keras_model_path)

# Convert the Keras model to TensorFlow Lite format
converter = tf.lite.TFLiteConverter.from_keras_model(keras_model)
tflite_model = converter.convert()

# Save the converted model to disk
with open(tflite_model_path, 'wb') as f:
    f.write(tflite_model)


print("done")

