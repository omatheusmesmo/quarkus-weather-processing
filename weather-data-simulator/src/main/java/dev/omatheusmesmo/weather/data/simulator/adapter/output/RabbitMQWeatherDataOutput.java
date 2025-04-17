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
            @Channel("weather.data.exchange")
    Emitter<WeatherData> weatherDataEmitter;

    @Override
    public void send(WeatherData weatherData){
        weatherDataEmitter.send(Message.of(weatherData)
                .withMetadata(Metadata.of("routing-key=weather.data")));
    }
}


//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//// Assuming a conceptual messaging library with these annotations
//import com.example.messaging.Channel;
//import com.example.messaging.Emitter;
//import com.example.messaging.Message;
//
//import com.example.domain.Order;
//
//@ApplicationScoped
//public class OrderPublisher {
//
//    @Inject
//    @Channel("order-channel") // This should match the configured channel name
//    Emitter<Order> orderEmitter;
//
//    public void publishOrder(Order order, String processingType) {
//        String routingKey;
//        switch (processingType) {
//            case "express":
//                routingKey = "order.express";
//                break;
//            case "standard":
//                routingKey = "order.standard";
//                break;
//            default:
//                routingKey = "order.default";
//        }
//
//        orderEmitter.send(Message.of(order)
//                .withMetadata(metadata -> metadata.with("routingKey", routingKey)));
//    }
//}