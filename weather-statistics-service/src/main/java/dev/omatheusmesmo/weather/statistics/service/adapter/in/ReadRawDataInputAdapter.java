package dev.omatheusmesmo.weather.statistics.service.adapter.in;

import dev.omatheusmesmo.weather.statistics.service.core.domain.RawWeatherDataReading;
import dev.omatheusmesmo.weather.statistics.service.core.port.in.ReadRawDataInputPort;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.inject.Inject;

@ApplicationScoped
public class ReadRawDataInputAdapter {

    @Inject
    ReadRawDataInputPort useCase;

    @Incoming("weather-data-in")
    public void consumeMessageFromBroker(RawWeatherDataReading rawWeatherDataReading) {
        useCase.receive(rawWeatherDataReading);
    }
}
