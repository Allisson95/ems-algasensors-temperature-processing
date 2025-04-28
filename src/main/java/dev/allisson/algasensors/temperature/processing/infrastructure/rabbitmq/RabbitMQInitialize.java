package dev.allisson.algasensors.temperature.processing.infrastructure.rabbitmq;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMQInitialize {

    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void initialize() {
        this.rabbitAdmin.initialize();
    }

}
