package dev.allisson.algasensors.temperature.processing.api.controller;

import dev.allisson.algasensors.temperature.processing.api.model.TemperatureLog;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
public class TemperatureProcessingController {

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID sensorId, @RequestBody String input) {
        log.info("Received temperature data for sensor {}: {}", sensorId, input);
        if (input == null || input.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        double temperature;
        try {
            temperature = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final TemperatureLog temperatureLog = new TemperatureLog(sensorId, temperature);

        log.info("Saving temperature log for sensor {}: {}", sensorId, temperatureLog);
    }

}
