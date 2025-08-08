package dev.omatheusmesmo.weather.statistics.service.core.usecase;

import dev.omatheusmesmo.weather.statistics.service.core.domain.RawWeatherDataReading;
import dev.omatheusmesmo.weather.statistics.service.core.port.in.ReadRawDataInputPort;
import dev.omatheusmesmo.weather.statistics.service.core.port.out.RawDataHistoryOutputPort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcessRawDataUseCase implements ReadRawDataInputPort {

    @Inject
    RawDataHistoryOutputPort historyOutputPort;

    @Override
    public void receive(RawWeatherDataReading rawWeatherDataReading) {
        historyOutputPort.save(rawWeatherDataReading);
    }
}
