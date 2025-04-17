package dev.omatheusmesmo.weather.data.simulator.core.usecase;

import dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherData;

public interface WeatherGenerator {

    WeatherData generateWeatherData();
}