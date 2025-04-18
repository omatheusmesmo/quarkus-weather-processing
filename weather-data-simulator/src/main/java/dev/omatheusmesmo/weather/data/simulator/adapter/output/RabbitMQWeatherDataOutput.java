package dev.omatheusmesmo.weather.data.simulator.adapter.output;

import dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherData;
import dev.omatheusmesmo.weather.data.simulator.core.ports.WeatherDataOutput;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

@ApplicationScoped
public class RabbitMQWeatherDataOutput implements WeatherDataOutput {

    @Inject
            @Channel("weather-data-exchange")
    Emitter<WeatherData> weatherDataEmitter;

    @Override
    public void send(WeatherData weatherData){
        weatherDataEmitter.send(Message.of(weatherData)
                .withMetadata(Metadata.of("routing-key=weather.data")));
    }
}