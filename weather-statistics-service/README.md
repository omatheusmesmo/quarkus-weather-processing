# Weather Statistics Service

This repository contains the code for the **Weather Statistics Service**, one of two microservices in the Real-time Weather Data Processing Pipeline designed to provide a farm with valuable insights into regional weather patterns.

The Weather Statistics Service consumes raw weather data, performs sophisticated time-windowed and cumulative aggregations, and provides a complete history of raw readings for detailed analysis.

## Project Overview

The Real-time Weather Data Processing Pipeline simulates a real-time weather monitoring system. It consists of:

1.  **Weather Data Simulator:** Generates synthetic raw weather data.
2.  **Weather Statistics Service:** Consumes raw data, calculates advanced statistics, persists data history, and publishes results.

Both services communicate via RabbitMQ and leverage the ELK Stack for observability.

## Architecture

The **Weather Statistics Service** follows a **Hexagonal Architecture** to clearly separate core business logic (aggregation, state management) from external concerns (messaging, databases, logging).

* **Core:** Domain and Use Cases for processing and aggregation.
* **Ports:** Interfaces defining interactions (consuming data, publishing results/logs, persisting state/history).
* **Adapters:** Implementations for specific technologies (RabbitMQ, PostgreSQL, MongoDB, Elasticsearch).

## Features

The Weather Statistics Service provides the following key capabilities:

* **Real-time Raw Data Consumption:** Consumes raw weather data points from a RabbitMQ queue.
* **Sophisticated Time Window Aggregations:** Calculates detailed statistics over defined time intervals, including:
    * Average, Min, Max, and Rate of Change for temperature, humidity, and pressure.
    * Counts of readings per sensor and poor air quality occurrences.
    * Counts and Duration of critical temperature events (e.g., frost, heat stress).
* **Cumulative Agronomic Metrics:** Tracks and reports cumulative values over longer periods, such as Growing Degree Days (GDD).
* **Complete Raw Data History:** Reliably stores every incoming raw weather data reading in a NoSQL database for long-term retention and future analysis.
* **Structured Aggregated Output:** Publishes calculated statistics in a structured JSON format to another RabbitMQ queue.
* **Operational Monitoring:** Sends application logs to Elasticsearch for centralized monitoring and analysis.

## Technology Stack

* Java 21
* Quarkus
* Maven
* RabbitMQ (via SmallRye Reactive Messaging)
* PostgreSQL (for Cumulative State - GDD)
* MongoDB (for Raw Data History)
* Elasticsearch (for Logging)

## Getting Started

These instructions will help you set up and run the Weather Statistics Service locally.

### Prerequisites

* Java Development Kit (JDK) 21
* Maven
* Docker (Recommended for running RabbitMQ, PostgreSQL, MongoDB, and ELK)
* Running instances of:
    * RabbitMQ broker
    * PostgreSQL database
    * MongoDB database
    * Elasticsearch and Kibana (ELK Stack)

### Building

Navigate to the service's root directory in your terminal and run:

```bash
# Using Maven Wrapper
./mvnw clean package
```

This command compiles the code, runs tests, and packages the application.

### Running Locally

After building, you can run the application. Ensure your required infrastructure components (RabbitMQ, PostgreSQL, MongoDB, Elasticsearch) are running and accessible.

```bash
# Run the executable JAR
java -jar target/quarkus-app/quarkus-run.jar
```

The service will connect to the configured RabbitMQ queue (`raw-data-queue`), databases, and Elasticsearch upon startup.

### Configuration

Database connection details, RabbitMQ channel names and connection details, and Elasticsearch logging configuration are managed in the `src/main/resources/application.properties` file. Update this file with your specific infrastructure connection details.

### Deployment

Deployment to Kubernetes or other container orchestration platforms would require creating Docker images and Kubernetes manifests (Deployment, Service), configuring external access to infrastructure services, etc. (Details for deployment are outside the scope of this basic README).

## Potential Future Enhancements

* Implement tools for querying and visualizing the raw data history stored in MongoDB.
* Develop an alerting system based on sophisticated metrics.
* Integrate with predictive models using historical data.
* Enhance monitoring with metrics collection (e.g., Prometheus) and visualization (e.g., Grafana).
* Add robust error handling, including Dead Letter Queues for RabbitMQ.

## Contributing

(Section for contribution guidelines)

## License

(Section for license information)

## Related Guides

- MongoDB client ([guide](https://quarkus.io/guides/mongodb)): Connect to MongoDB in either imperative or reactive style
- Elasticsearch Java Client ([guide](https://quarkus.io/guides/elasticsearch)): Connect to an Elasticsearch cluster using the Java client
- Messaging - RabbitMQ Connector ([guide](https://quarkus.io/guides/rabbitmq)): Connect to RabbitMQ with Reactive Messaging
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
