# Real-time Weather Data Processing Microservices

## Overview

This project consists of two microservices designed to simulate a real-time weather data processing pipeline, both built using Quarkus (Java 21). The **Weather Data Simulator** generates synthetic weather data and publishes it to a RabbitMQ queue. The **Weather Statistics Service** consumes this raw data from RabbitMQ, performs complex aggregations over a time window, and publishes the aggregated results to another RabbitMQ queue. Additionally, the project incorporates observability using the ELK Stack for centralized logging.

This setup demonstrates key skills in building modern microservices using Quarkus (Java 21), Kubernetes, and RabbitMQ, showcasing capabilities in data streaming, message queuing, data processing, container orchestration, and observability.

## Architecture

The **Weather Statistics Service** will follow a full Hexagonal Architecture:

* **Core (Domain):** Contains the main business logic for data aggregation.
* **Ports:**
    * **Input Ports:** Define operations triggered from outside (e.g., processing raw data).
    * **Output Ports:** Define dependencies on the outside world (e.g., publishing aggregated data, logging).
* **Adapters:**
    * **Input Adapters:** Handle external communication (e.g., RabbitMQ consumer) and call Input Ports.
    * **Output Adapters:** Interact with external systems (e.g., RabbitMQ publisher, Elasticsearch for logging) and are called by Output Ports.

The **Weather Data Simulator** will be a simple Quarkus application that periodically sends weather data to RabbitMQ.

## Microservices

### 1. Weather Data Simulator

* **Purpose:** Generates simulated weather data (temperature, humidity, pressure, air quality) and publishes it to a RabbitMQ queue.
* **Technology:** Implemented using Quarkus (Java 21).
* **Output JSON Format (Published to RabbitMQ):**

    ```json
    {
      "timestamp": "2025-04-16T10:30:00Z",
      "sensor_id": "sensor-123",
      "temperatura": 25.5,
      "umidade": 60.2,
      "pressao": 1012.5,
      "qualidade_do_ar": "boa"
    }
    ```

* **RabbitMQ Interaction:**
    * **Exchange:** (Using the default exchange for simplicity initially)
    * **Routing Key (or Queue Name):** `raw-data-queue`
    * The simulator will publish messages with the above JSON format to the `raw-data-queue`.

### 2. Weather Statistics Service

* **Purpose:** Consumes raw weather data from RabbitMQ, performs aggregations (average, min, max for temperature, humidity, pressure; count of readings per sensor; count of poor air quality readings) over a defined time window (e.g., every 5 seconds), and publishes the aggregated results to another RabbitMQ queue. Additionally, it will send logs to Elasticsearch for centralized logging.
* **Technology:** Implemented using Quarkus (Java 21).
* **Input JSON Format (Consumed from RabbitMQ):**

    ```json
    {
      "timestamp": "2025-04-16T10:30:00Z",
      "sensor_id": "sensor-123",
      "temperatura": 25.5,
      "umidade": 60.2,
      "pressao": 1012.5,
      "qualidade_do_ar": "boa"
    }
    ```

* **Output JSON Format (Published to RabbitMQ):**

    ```json
    {
      "aggregation_timestamp": "2025-04-16T10:30:05Z",
      "window_start": "2025-04-16T10:30:00Z",
      "window_end": "2025-04-16T10:30:05Z",
      "temperatura": {
        "average": 25.3,
        "min": 24.9,
        "max": 25.8
      },
      "umidade": {
        "average": 60.5,
        "min": 60.0,
        "max": 61.0
      },
      "pressao": {
        "average": 1012.3,
        "min": 1012.0,
        "max": 1012.7
      },
      "sensor_readings_count": {
        "sensor-123": 5,
        "sensor-456": 3
      },
      "poor_air_quality_count": 1
    }
    ```

* **RabbitMQ Interaction:**
    * **Consume from Queue:** `raw-data-queue`
    * **Publish to Queue:** `aggregated-data-queue`
    * The service will consume messages from `raw-data-queue`, perform aggregations based on a time window, and publish the results in the above JSON format to the `aggregated-data-queue`.

* **Logging:** Logs will be sent to Elasticsearch for centralized management and analysis using Kibana.

## RabbitMQ Setup

We will need a RabbitMQ instance running. The following queues will be used:

* `raw-data-queue`: To receive raw weather data.
* `aggregated-data-queue`: To publish the aggregated weather statistics.

For simplicity, we can start by using the default exchange and binding these queues with their names as routing keys.

## Technology Stack

* Quarkus (Java 21)
* Java 21
* Kubernetes (for container orchestration)
* Docker (for containerization)
* RabbitMQ (for message queuing)
* Elasticsearch, Logstash, Kibana (ELK Stack) (for observability - logging)

## Development Guide

### Prerequisites

* Java Development Kit (JDK) 21
* Maven
* Docker
* Kubernetes cluster (Minikube, Kind, or a cloud provider cluster) - optional for local development
* Running instance of RabbitMQ
* Running instance of Elasticsearch and Kibana (and optionally Logstash)

### Building and Running Locally

**1. Weather Data Simulator (Quarkus - Java 21)**

   * Create a new Quarkus project for the simulator (ensure Java 21 is configured in your Maven setup).
   * Implement a service that periodically generates weather data and sends it to the `raw-data-queue` using the `quarkus-reactive-messaging-rabbitmq` extension.
   * Configure logging (e.g., using Logback) to output logs in a format that can be ingested by Elasticsearch (e.g., JSON).

**2. Weather Statistics Service (Quarkus - Java 21)**

   * Implement the logic to consume from `raw-data-queue`, perform aggregations, and publish to `aggregated-data-queue`.
   * Configure logging to send logs to Elasticsearch.

### Deploying to Kubernetes

* Create Dockerfiles for both the simulator and the aggregator applications (ensure they use a Java 21 base image).
* Build Docker images and push them to a container registry.
* Create Kubernetes Deployment manifests for both services.
* Create Kubernetes Service manifests (if needed).
* You might also need to deploy the ELK Stack in your Kubernetes cluster.
* Configure the Quarkus applications to send logs to the Elasticsearch service within Kubernetes.

## Potential Improvements

* Implement more sophisticated data aggregation techniques.
* Add error handling and retry mechanisms for RabbitMQ communication.
* Implement metrics collection using Quarkus extensions for Prometheus and visualize them with Grafana.
* Secure the communication between the services and RabbitMQ.
* Explore different RabbitMQ exchange types and routing strategies for more complex data flow.
* Implement unit and integration tests for the aggregation logic.
* Set up a proper CI/CD pipeline for building and deploying the microservices.
* Implement distributed tracing with Jaeger or Zipkin.
