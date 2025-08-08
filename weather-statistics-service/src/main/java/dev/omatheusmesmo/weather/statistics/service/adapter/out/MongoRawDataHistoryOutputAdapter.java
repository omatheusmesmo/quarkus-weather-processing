package dev.omatheusmesmo.weather.statistics.service.adapter.out;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import dev.omatheusmesmo.weather.statistics.service.core.domain.RawWeatherDataReading;
import dev.omatheusmesmo.weather.statistics.service.core.port.out.RawDataHistoryOutputPort;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MongoRawDataHistoryOutputAdapter implements RawDataHistoryOutputPort {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "mongodb.database.name")
    String databaseName;

    @ConfigProperty(name = "mongodb.collection.raw-data.name")
    String collectionName;

    private MongoCollection<Document> collection;

    @PostConstruct
    void init() {
        this.collection = mongoClient.getDatabase(databaseName).getCollection(collectionName, Document.class);
    }

    @Override
    public void save(RawWeatherDataReading rawWeatherDataReading) {
        Document document = new Document()
                .append("timestamp", rawWeatherDataReading.timestamp())
                .append("sensorId", rawWeatherDataReading.sensorId())
                .append("temperature", rawWeatherDataReading.temperature())
                .append("humidity", rawWeatherDataReading.humidity())
                .append("pressure", rawWeatherDataReading.pressure())
                .append("airQuality", rawWeatherDataReading.airQuality());

        collection.insertOne(document);
    }
}