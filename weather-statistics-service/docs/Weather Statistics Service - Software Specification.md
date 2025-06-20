# Weather Statistics Service - Software Specification

## 1. Introduction

This document specifies the design and technical details for the Weather Statistics Service, a microservice within the Real-time Weather Data Processing Pipeline. Enhanced to provide sophisticated aggregations and persist the raw data history in a NoSQL database, it consumes raw weather data, performs time-windowed and cumulative calculations, and publishes these enhanced insights. The service is built using Quarkus (Java 21) and adheres to Hexagonal Architecture principles.

## 2. Architecture: Hexagonal Design

The Weather Statistics Service follows Hexagonal Architecture, ensuring a clear separation. The Core's aggregation logic is specifically designed for the available input fields.

* **Core (Domain):** Encapsulates the business rules and logic for both basic and advanced weather data aggregation and defines the need for persisting raw data history and cumulative state.
* **Ports:** Define the interfaces through which the core interacts with external dependencies.
    * **Input Ports:** For processing incoming raw weather data messages.
    * **Output Ports:** Define interfaces for publishing aggregated results, logging, persisting cumulative state (GDD), and persisting raw data history (NoSQL).
* **Adapters:** Implement the Ports and handle communication with specific external technologies (RabbitMQ consumer/publisher, Elasticsearch logging, Database adapter for cumulative state, NoSQL Database adapter for raw history).

## 3. Microservice Details: Weather Statistics Service

* **Purpose:** To receive a stream of raw weather data points, perform sophisticated aggregations (both within defined time windows and cumulatively for GDD and event duration cumulatively for GDD and event duration), persist a complete history of raw readings, and publish detailed statistical outputs.
* **Technology Stack:** Quarkus (Java 21), leveraging the SmallRye Reactive Messaging connector for RabbitMQ, logging libraries integrated with Elasticsearch, a Database extension for cumulative state persistence, and a NoSQL Database extension for raw data history.
* **Input Data Source:** Consumes raw weather data messages from a RabbitMQ queue (`raw-data-queue`).
* **Input Data Format:** Expects the raw weather data in the following exact JSON format:

    ```json
    {
       "timestamp": "YYYY-MM-DDTHH:mm:ss", // e.g., "2023-10-01T12:00:00"
       "sensorId": 1, // Numeric sensor identifier
       "temperature": 25.5, // e.g., Celsius
       "humidity": 60.0, // e.g., Percentage
       "pressure": 1013.25, // e.g., hPa
       "airQuality": "GOOD" // e.g., "GOOD", "MODERATE", "POOR", etc.
    }
    ```
  *(Note: The `timestamp` format should be confirmed based on the actual producer.)*

* **Processing Logic:**
    * Reads messages from the input queue.
    * **Persist Raw Data History:** For each incoming raw data point, saves the complete JSON document (or a mapped object) to the NoSQL database via the `RawDataHistoryPort`.
    * Collects data points within a defined time window (e.g., 5-minute intervals).
    * **Performs Time-Windowed Aggregations:**
        * Average, minimum, and maximum for `temperature`, `humidity`, and `pressure` within each window.
        * Rate of change for `temperature`, `humidity`, and `pressure` within the window.
        * Count of readings per unique `sensorId` within the window.
        * Count of readings where `airQuality` indicates "POOR" within the window.
        * Count of occurrences where `temperature` crossed specific thresholds (e.g., below 0°C for frost, above 30°C for heat stress) within the window.
        * Duration of Threshold Events: Track the total time spent within/outside thresholds within a window.
    * **Performs Cumulative Aggregations (Requires State Management):**
        * Growing Degree Days (GDD): Calculates the daily GDD increment based on `temperature` and adds it to the cumulative value, persisted via the `CumulativeWeatherDataStatePort`.
    * Triggers the aggregation calculation and publication at the end of each time window.
* **Output Data Destination:** Publishes the aggregated statistics to a different RabbitMQ queue (`aggregated-data-queue`).
* **Output Data Format:** Publishes aggregated weather statistics in JSON format:

    ```json
    {
      "aggregationTimestamp": "YYYY-MM-DDTHH:mm:ssZ",
      "windowStart": "YYYY-MM-DDTHH:mm:ssZ",
      "windowEnd": "YYYY-MM-DDTHH:mm:ssZ",
      "temperature": {
        "average": 0.0, "min": 0.0, "max": 0.0, "rateOfChange": 0.0
      },
      "humidity": {
        "average": 0.0, "min": 0.0, "max": 0.0, "rateOfChange": 0.0
      },
      "pressure": {
        "average": 0.0, "min": 0.0, "max": 0.0, "rateOfChange": 0.0
      },
      "sensorReadingsCount": {
        "string": 0 // Count per sensorId
      },
      "poorAirQualityCount": 0,
      "frostEventsCount": 0, // e.g., times temp went below 0C in the window
      "heatStressEventsCount": 0, // e.g., times temp went above 30C in the window
      "durationBelowFreezingMinutes": 0.0, // Total minutes temp was below 0C in the window
      "cumulativeGrowingDegreeDays": 0.0 // Cumulative since a reset point (e.g., season start)
      // ... other sophisticated metrics derived from available input fields
    }
    ```
* **Raw Data History Storage:** Persists every incoming raw weather data point in a NoSQL database for long-term storage and future analysis.
* **RabbitMQ Interaction Details:** Consuming from `raw-data-queue` and publishing to `aggregated-data-queue`.
* **Logging:** Utilizes an Output Adapter to send application logs to Elasticsearch.

## 4. Mathematical Calculations

The following mathematical calculations are performed:

* **Average:** Sum of values in the window / Count of values in the window.
    * `Average Temp = (Σ temperature) / (Count of readings)`
    * `Average Humidity = (Σ humidity) / (Count of readings)`
    * `Average Pressure = (Σ pressure) / (Count of readings)`
* **Minimum (Min):** The smallest value recorded in the window.
* **Maximum (Max):** The largest value recorded in the window.
* **Rate of Change:** Calculated as the difference between the last and first reading in the window divided by the time elapsed in the window. Can also be calculated between consecutive readings.
    * `Rate of Change = (Value at End of Window - Value at Start of Window) / (Time at End of Window - Time at Start of Window)`
* **Count of Threshold Events:** Simple counter incremented each time a reading meets or exceeds a specific threshold within the window.
* **Duration of Threshold Events:** Sum of the time intervals during which a reading meets or exceeds a specific threshold within the window. Requires tracking when the condition starts and ends.
* **Count of Readings per Sensor:** Simple counter, tallying how many readings are received for each unique `sensorId` within the window.
* **Count of Poor Air Quality:** Simple counter, tallying how many readings have `airQuality` equal to "POOR" within the window.
* **Growing Degree Days (GDD):** A common formula for GDD (modified for discrete readings) is:
    * Daily Max Temp (in window) = Max `temperature` reading in the day's window(s)
    * Daily Min Temp (in window) = Min `temperature` reading in the day's window(s)
    * Base Temperature (Tb) = A fixed value dependent on the crop (e.g., 10°C for corn).
    * `Daily GDD Increment = ((Daily Max Temp + Daily Min Temp) / 2) - Tb`
    * If the result is less than 0, the Daily GDD Increment is 0.
    * `Cumulative GDD = Cumulative GDD from previous day + Daily GDD Increment`
    * *(Note: Accurate GDD calculation often requires daily min/max. This implementation aggregates based on potentially shorter windows, so daily min/max would need to be tracked across windows within a day.)*

## 5. Technology Stack (Specific to this Service - Enhanced)

* Java 21
* Quarkus
* Maven
* RabbitMQ (via SmallRye Reactive Messaging)
* Elasticsearch (logging client library)
* Database for Cumulative State (e.g., PostgreSQL via Quarkus Hibernate ORM extension)
* NoSQL Database for Raw History (e.g., MongoDB via Quarkus MongoDB Client extension)

## 6. Development Guide (Specific to this Service - Enhanced)

### 6.1. Prerequisites

* JDK 21, Maven.
* Access to a RabbitMQ broker.
* Access to an Elasticsearch instance.
* Access to a Database for cumulative state persistence.
* Access to a NoSQL Database for raw history persistence.

### 6.2. Building and Running Locally

* Implement the enhanced Core logic for sophisticated aggregations and state management, adhering to the defined mathematical calculations.
* Define new Ports (`RawDataHistoryPort`, `CumulativeWeatherDataStatePort`) and update existing ones.
* Implement Adapters for RabbitMQ, Elasticsearch, the Database for cumulative state, and the NoSQL Database for raw history.
* Configure `application.properties` for all dependencies (RabbitMQ, Elasticsearch, Database, NoSQL Database).
* Implement JSON deserialization for the incoming message format.
* Build the service using Maven.
* Run the application locally, ensuring connectivity to all dependencies.

## 7. Potential Technical Improvements

* Refine the time windowing logic and state management for accuracy, especially for GDD and duration metrics across window boundaries.
* Implement dead-letter queue handling for messages that fail processing.
* Optimize performance for high-throughput data ingestion into the NoSQL history database.
* Add monitoring for the health and performance of both database connections.
* Implement robust handling of potential errors during data deserialization, calculation, or database interactions.

---