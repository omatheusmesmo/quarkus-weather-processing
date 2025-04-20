# Weather Data Simulator

This project is a weather data simulator built using **Quarkus**, the Supersonic Subatomic Java Framework. It generates random weather data and sends it to RabbitMQ for further processing.

## Features

- **Random Weather Data Generation**: Generates weather data with random values for temperature, humidity, pressure, air quality, and sensor ID.
- **RabbitMQ Integration**: Sends generated weather data to RabbitMQ using reactive messaging.
- **REST API**: Exposes an endpoint to trigger the generation of random weather data.

## Project Structure

The project follows a clean architecture approach with the following layers:

- **Core Domain**: Contains the business logic and domain models (`WeatherData`, `AirQuality`, etc.).
- **Use Cases**: Implements the application logic, such as generating weather data (`GenerateWeatherDataUseCase`).
- **Adapters**:
    - **Input**: REST API (`WeatherDataResource`) to trigger weather data generation.
    - **Output**: RabbitMQ integration (`RabbitMQWeatherDataOutput`) to send weather data.

## REST API

### Generate Random Weather Data

**Endpoint**: `GET /weather/generate/random`  
**Description**: Generates a random number (1-10) of weather data messages and sends them to RabbitMQ.  
**Response**: Returns the number of messages created and sent.

## Running the Application

### Development Mode

Run the application in development mode with live coding enabled:

```shell
./mvnw quarkus:dev
```
Access the Quarkus Dev UI at <http://localhost:8080/q/dev/>.

### Packaging and Running

Package the application:

```shell
./mvnw package
```

Run the application:

```shell
java -jar target/quarkus-app/quarkus-run.jar
```

### Building a Native Executable

Build a native executable:

```shell
./mvnw package -Dnative
```

Or use a containerized build:

```shell
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Run the native executable:

```shell
./target/weather-data-simulator-1.0.0-SNAPSHOT-runner
```

## RabbitMQ Configuration

The application uses the **RabbitMQ Connector** for reactive messaging. Ensure RabbitMQ is running and properly configured. The messages are sent to the `weather-data-exchange` with the routing key `weather.data`.

For more details, refer to the [RabbitMQ Connector Guide](https://quarkus.io/guides/rabbitmq).

## Related Projects

This project is part of the repository [quarkus-weather-processing](https://github.com/omatheusmesmo/quarkus-weather-processing) and is located in the `weather-data-simulator` folder.

## Contact

For any questions or issues, feel free to contact me at **matheus.6148@gmail.com**.