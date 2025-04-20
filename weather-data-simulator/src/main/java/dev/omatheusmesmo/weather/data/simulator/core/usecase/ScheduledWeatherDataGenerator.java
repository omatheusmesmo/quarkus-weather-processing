package dev.omatheusmesmo.weather.data.simulator.core.usecase;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import java.util.random.RandomGenerator;

@ApplicationScoped
public class ScheduledWeatherDataGenerator {

    @Inject
    GenerateWeatherDataUseCase generateWeatherDataUseCase;

    int maxMessages = 1;

    private final RandomGenerator rng = RandomGenerator.getDefault();

    @Scheduled(every = "5s")
    void generateWeatherDataPeriodically() {
        int numberOfMessages = rng.nextInt(1, maxMessages + 1);

        for (int i = 0; i < numberOfMessages; i++) {
            generateWeatherDataUseCase.execute();
        }
    }
}