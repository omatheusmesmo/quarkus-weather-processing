package dev.omatheusmesmo.weather.data.simulator.adapter.output;

import dev.omatheusmesmo.weather.data.simulator.core.domain.WeatherData;
import dev.omatheusmesmo.weather.data.simulator.core.ports.WeatherDataOutput;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
// Removido import não usado de Metadata genérico
import java.util.UUID; // Importar UUID

@ApplicationScoped
public class RabbitMQWeatherDataOutput implements WeatherDataOutput {

    @Inject
    @Channel("weather-data-exchange")
    Emitter<WeatherData> weatherDataEmitter;

    @Override
    public void send(WeatherData weatherData) {

        final OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
                .withRoutingKey("weather.data")
                .withContentType("application/json")
                .withHeader("message-id", UUID.randomUUID().toString())
                .withHeader("sensor-id", String.valueOf(weatherData.sensorId()))
                .withHeader("air-quality", weatherData.airQuality().name())
                .build();

        Message<WeatherData> message = Message.of(weatherData)
                .addMetadata(metadata);

        try {
            weatherDataEmitter.send(message);
        } catch (Exception e) {
            System.err.println("ERROR sending message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}