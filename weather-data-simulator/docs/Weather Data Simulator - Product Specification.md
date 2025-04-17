## Weather Data Simulator - Business Rules (PO Perspective)

As the Product Owner, I envision the **Weather Data Simulator** to fulfill the following key requirements:

1.  **Purpose:** The primary goal of this service is to generate realistic-looking synthetic weather data for our real-time weather data processing pipeline. This data will serve as the input for our **Weather Statistics Service**.

2.  **Data Generation:** The simulator must be capable of generating weather data that includes the following fields:
    * `timestamp`: A timestamp indicating when the data was generated. This should be in a standard format (e.g., ISO 8601 UTC).
    * `sensor_id`: A unique identifier for the simulated weather sensor. We should be able to simulate data from multiple sensors.
    * `temperatura`: The temperature reading in Celsius (or Fahrenheit, we can decide this). Let's go with Celsius for now.
    * `umidade`: The humidity level as a percentage.
    * `pressao`: The atmospheric pressure in hPa (Hectopascals).
    * `qualidade_do_ar`: A qualitative indicator of air quality (e.g., "boa", "moderada", "ruim").

3.  **Frequency of Data Generation:** The simulator should generate and publish this weather data periodically. For our initial setup, let's aim for a configurable interval, perhaps defaulting to generating data every second.

4.  **Data Format:** All generated weather data must be formatted as a JSON object, adhering to the structure defined in our `README.md`.

5.  **Target System:** The generated JSON data should be published to the RabbitMQ queue named `raw-data-queue`.

6.  **Simulating Multiple Sensors (Optional but Recommended):** To make the data more realistic and to prepare for more complex scenarios in the **Weather Statistics Service**, the simulator should be capable of generating data for multiple distinct `sensor_id` values.