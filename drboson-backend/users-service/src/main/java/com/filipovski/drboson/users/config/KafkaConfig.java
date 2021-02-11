package com.filipovski.drboson.users.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:messaging/messaging.properties")
public class KafkaConfig {

    @Value("${kafka.users.topic}")
    private String usersTopic;
}
