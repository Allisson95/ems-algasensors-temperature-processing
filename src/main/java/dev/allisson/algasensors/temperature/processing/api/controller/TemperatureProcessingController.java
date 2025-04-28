package dev.allisson.algasensors.temperature.processing.api.controller;

import dev.allisson.algasensors.temperature.processing.api.model.TemperatureLog;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static dev.allisson.algasensors.temperature.processing.infrastructure.rabbitmq.RabbitMQConfiguration.FANOUT_EXCHANGE_NAME;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
public class TemperatureProcessingController {

    private final RabbitTemplate rabbitTemplate;

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
            log.warn("Error parsing temperature data for sensor {}: {}", sensorId, input);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final TemperatureLog temperatureLog = new TemperatureLog(sensorId, temperature);

        this.rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_NAME, "", temperatureLog);
        log.trace("Temperature data sent to processing: {}", temperatureLog);
    }

}
