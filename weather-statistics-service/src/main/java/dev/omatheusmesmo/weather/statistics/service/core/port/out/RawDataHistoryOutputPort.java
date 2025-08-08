package dev.omatheusmesmo.weather.statistics.service.core.port.out;

import dev.omatheusmesmo.weather.statistics.service.core.domain.RawWeatherDataReading;

public interface RawDataHistoryOutputPort {

    void save(RawWeatherDataReading rawWeatherDataReading);
}
