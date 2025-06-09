package dev.omatheusmesmo.weather.statistics.service.core.domain;

import java.math.BigDecimal;

public record NumericalStatistics(
        BigDecimal min,
        BigDecimal average,
        BigDecimal max,
        BigDecimal rateOfChange
) {
}
