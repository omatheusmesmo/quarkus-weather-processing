package dev.omatheusmesmo.weather.data.simulator.adapter.input.rest;

import dev.omatheusmesmo.weather.data.simulator.core.usecase.GenerateWeatherDataUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.random.RandomGenerator;

@Path("/weather")
public class WeatherDataResource {

    @Inject
    GenerateWeatherDataUseCase generateWeatherDataUseCase;

    @GET
    @Path("/generate/random")
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateRandomAmount() {

        int numberOfMessages = RandomGenerator.getDefault().nextInt(1,11);

        for (int i = 0; i < numberOfMessages; i++) {
            generateWeatherDataUseCase.execute();
        }

        return Response.ok(numberOfMessages + " messages created and send").build();
    }
}