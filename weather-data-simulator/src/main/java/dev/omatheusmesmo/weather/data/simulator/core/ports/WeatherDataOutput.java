package dev.omatheusmesmo.weather.data.simulator.core.ports;

import dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherData;

public interface WeatherDataOutput {

    void send(WeatherData weatherData);
}
