package dev.allisson.algasensors.temperature.processing.common;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import java.util.UUID;

public final class IdGenerator {
    private static final TimeBasedEpochRandomGenerator TIME_BASED_EPOCH_RANDOM_GENERATOR =
            Generators.timeBasedEpochRandomGenerator();

    private IdGenerator() {
        // Prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UUID generateId() {
        return TIME_BASED_EPOCH_RANDOM_GENERATOR.generate();
    }
}
