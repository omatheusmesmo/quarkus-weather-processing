package dev.omatheusmesmo.weather.statistics.service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RawWeatherDataReading(
        LocalDateTime timestamp,
        Integer sensorId,
        BigDecimal temperature,
        BigDecimal humidity,
        BigDecimal pressure,
        String airQuality
) {
}