package dev.omatheusmesmo.weather.data.simulator.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeatherData(
        LocalDateTime timestamp,
        Long sensorId,
        BigDecimal temperature,
        BigDecimal humidity,
        BigDecimal pressure,
        AirQuality airQuality
) { }
