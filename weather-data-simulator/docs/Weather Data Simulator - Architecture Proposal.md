## Weather Data Simulator - Architecture Proposal (Software Architect Perspective)

From an architectural standpoint, the **Weather Data Simulator** will be a straightforward Quarkus application designed with simplicity and efficiency in mind:

1.  **Technology Stack:** We will be using Java 21 and the Quarkus framework.

2.  **Quarkus Extensions:** The project will utilize the following Quarkus extensions:
    * `quarkus-reactive-messaging-rabbitmq`: To facilitate the connection to and message publishing on RabbitMQ using Reactive Messaging.
    * `quarkus-resteasy-jackson`: To handle the serialization of our Java objects into JSON format.

3.  **Core Component:** We will have a Java class (let's call it `WeatherSimulator`) responsible for:
    * Generating the synthetic weather data. This will involve creating random values within realistic ranges for temperature, humidity, pressure, and selecting from a predefined set for air quality. It will also assign a timestamp and a sensor ID.
    * Formatting the generated data into the required JSON structure.

4.  **RabbitMQ Integration:** We will configure a channel using the Reactive Messaging API to connect to the `raw-data-queue` in RabbitMQ. The `WeatherSimulator` will use an `@Outgoing` annotation to send the generated JSON data to this channel.

5.  **Scheduling:** We will use Quarkus's `@Scheduled` annotation to trigger the `WeatherSimulator` to generate and send data at the configured interval (e.g., every second). This annotation will allow us to define a method that runs periodically.

6.  **Configuration:** We will use Quarkus's configuration system (likely `application.properties` or `application.yaml`) to configure:
    * The RabbitMQ connection details (host, port, username, password, etc.).
    * The interval for data generation.
    * Potentially a list of `sensor_id` values to simulate.

7.  **Logging:** We will configure a basic logging setup (initially to the console) using Quarkus's default logging mechanism. We will address the integration with Elasticsearch for logging in a later phase.

This architecture provides a clear and simple approach to meet the business requirements for the **Weather Data Simulator**. It leverages the strengths of Quarkus for building efficient and reactive applications.