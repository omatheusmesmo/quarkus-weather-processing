## Weather Data Simulator - Product Specification (PO Perspective)

The **Weather Data Simulator** will meet the following requirements:

1. **Purpose:**
    - Generate realistic synthetic weather data for the real-time processing pipeline.

2. **Data Fields:**
    - `timestamp`: ISO 8601 UTC format.
    - `sensorId`: Unique identifier for the sensor.
    - `temperature`: Temperature in Celsius.
    - `humidity`: Relative humidity as a percentage.
    - `pressure`: Atmospheric pressure in hPa.
    - `airQuality`: Air quality indicator (`GOOD`, `MEDIUM`, `BAD`).

3. **Frequency:**
    - Configurable interval for data generation (default: 1 second).

4. **Data Format:**
       - JSON structure:
   ```json
   {
      "timestamp": "2023-10-01T12:00:00",
      "sensorId": 1,
      "temperature": 25.5,
      "humidity": 60.0,
      "pressure": 1013.25,
      "airQuality": "GOOD"
   }
   ```

5. **Target System:**
    - Publish data to the RabbitMQ queue `raw-data-queue`.

6. **Multi-Sensor Simulation:**
    - Support multiple `sensorId` values for realistic scenarios.

7. **Language Configuration:**
    - Ensure `airQuality` and other fields are in English.

8. **Validation:**
    - Validate generated data to ensure it falls within expected ranges.

9. **Non-Functional Requirements:**
    - Performance: Support up to 10 messages per second.
    - Fault Tolerance: Ensure retries for failed messages.

10. **Example Scenario:**
    - Simulate data flow from generation to RabbitMQ consumption for analytics.

These specifications ensure the simulator meets business and technical requirements effectively.