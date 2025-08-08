package dev.omatheusmesmo.weather.statistics.service.core.port.in;

import dev.omatheusmesmo.weather.statistics.service.core.domain.RawWeatherDataReading;

public interface ReadRawDataInputPort {

    void receive(RawWeatherDataReading rawWeatherDataReading);
}
