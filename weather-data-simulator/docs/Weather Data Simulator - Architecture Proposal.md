## Weather Data Simulator - Architecture Proposal (Software Architect Perspective)

From an architectural standpoint, the **Weather Data Simulator** will be a Quarkus-based application designed for simplicity, scalability, and efficiency:

1. **Technology Stack:**
    - Java 21 with the Quarkus framework.

2. **Quarkus Extensions:**
    - `quarkus-reactive-messaging-rabbitmq`: For RabbitMQ integration using Reactive Messaging.
    - `quarkus-resteasy-jackson`: For JSON serialization and RESTful APIs.

3. **Core Component:**
    - A `WeatherSimulator` class will:
        - Generate synthetic weather data with realistic ranges for temperature, humidity, pressure, and air quality.
        - Format the data into a JSON structure.

4. **RabbitMQ Integration:**
    - A channel will be configured using the Reactive Messaging API to connect to the `raw-data-queue` in RabbitMQ.
    - Messages will include metadata such as `message-id`, `sensor-id`, and `air-quality`.

5. **Scheduling:**
    - Use Quarkus's `@Scheduled` annotation to periodically generate and send data at configurable intervals.

6. **Configuration:**
    - Use `application.properties` for:
        - RabbitMQ connection details (host, port, username, password).
        - Data generation interval.
        - Sensor ID ranges.

7. **Logging:**
    - Implement structured logging for events like message generation, RabbitMQ connection errors, and retries.

8. **Scalability:**
    - Support horizontal scaling by running multiple simulator instances.

9. **Error Handling:**
    - Implement automatic retries and reconnections for RabbitMQ failures.

10. **Security:**
    - Protect RabbitMQ credentials using environment variables or secret management tools.

11. **Testing:**
    - Include unit tests, integration tests, and load tests to ensure reliability and performance.

This architecture ensures a robust, scalable, and maintainable system for generating and publishing weather data.