package dev.omatheusmesmo.weather.data.simulator.core.usecase;

import dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherData;
import dev.omatheusmesmo.weather.data.simulator.core.ports.WeatherDataOutput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GenerateWeatherDataUseCase {

    private final DefaultWeatherGenerator weatherGenerator;
    private final WeatherDataOutput weatherDataOutput;

    @Inject
    public GenerateWeatherDataUseCase(DefaultWeatherGenerator weatherGenerator, WeatherDataOutput weatherDataOutput) {
        this.weatherGenerator = weatherGenerator;
        this.weatherDataOutput = weatherDataOutput;
    }

    public void execute() {
        WeatherData weatherData = weatherGenerator.generateWeatherData();
        weatherDataOutput.send(weatherData);
    }
}