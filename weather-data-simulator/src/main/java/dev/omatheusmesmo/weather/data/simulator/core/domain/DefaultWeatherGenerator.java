package dev.omatheusmesmo.weather.data.simulator.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.random.RandomGenerator;
import static dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherDataConstants.*;

public class DefaultWeatherGenerator implements WeatherGenerator {

    private final RandomGenerator rng = RandomGenerator.getDefault();

    @Override
    public WeatherData generateWeatherData() {
        return new WeatherData(
                generateTimestamp(),
                generateSensorId(),
                generateTemperature(),
                generateHumidity(),
                generatePressure(),
                generateAirQuality()
        );
    }

    private LocalDateTime generateTimestamp() {
        return LocalDateTime.now();
    }

    private Long generateSensorId() {
        return rng.nextLong(MIN_SENSOR_ID, MAX_SENSOR_ID);
    }

    private BigDecimal generateTemperature() {
        return BigDecimal.valueOf(rng.nextDouble(MIN_TEMPERATURE, MAX_TEMPERATURE));
    }

    private BigDecimal generateHumidity() {
        return BigDecimal.valueOf(rng.nextDouble(MIN_HUMIDITY, MAX_HUMIDITY));
    }

    private BigDecimal generatePressure() {
        return BigDecimal.valueOf(rng.nextDouble(MIN_PRESSURE, MAX_PRESSURE));
    }

    private AirQuality generateAirQuality() {
        AirQuality[] qualities = AirQuality.values();
        return qualities[rng.nextInt(qualities.length)];
    }
}