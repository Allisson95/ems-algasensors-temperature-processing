package dev.allisson.algasensors.temperature.processing.api.controller;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.allisson.algasensors.temperature.processing.api.model.TemperatureLog;
import dev.allisson.algasensors.temperature.processing.infrastructure.rabbitmq.RabbitMQConfiguration;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
public class TemperatureProcessingController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable final TSID sensorId, @RequestBody final String input) {
        log.info("Received temperature data for sensor {}: {}", sensorId, input);
        if (input == null || input.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        double temperature;
        try {
            temperature = Double.parseDouble(input);
        } catch (final NumberFormatException e) {
            log.warn("Error parsing temperature data for sensor {}: {}", sensorId, input);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        final TemperatureLog temperatureLog = new TemperatureLog(sensorId, temperature);

        final MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("sensorId", sensorId.toString());
            return message;
        };

        this.rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.FANOUT_EXCHANGE_NAME,
                "",
                temperatureLog,
                messagePostProcessor);
        log.trace("Temperature data sent to processing: {}", temperatureLog);
    }

}
