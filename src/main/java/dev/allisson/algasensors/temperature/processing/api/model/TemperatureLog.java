package dev.allisson.algasensors.temperature.processing.api.model;

import dev.allisson.algasensors.temperature.processing.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class TemperatureLog {
    private final UUID id;
    private final TSID sensorId;
    private final Double temperature;
    private final Instant registeredAt;

    public TemperatureLog(TSID sensorId, Double temperature) {
        this.id = IdGenerator.generateId();
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.registeredAt = Instant.now();
    }
}
