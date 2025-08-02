package dev.omatheusmesmo.weather.statistics.service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record AggregatedWeatherStatistics(
        LocalDateTime aggregationTimestamp,
        LocalDateTime windowStart,
        LocalDateTime windowEnd,
        NumericalStatistics temperature,
        NumericalStatistics humidity,
        NumericalStatistics pressure,
        Map<String, Integer> sensorReadingsCount,
        Integer poorAirQualityCount,
        Integer frostEventsCount,
        Integer heatStressEventsCount,
        BigDecimal durationBelowFreezingMinutes,
        BigDecimal cumulativeGrowingDegreeDays
) {
}