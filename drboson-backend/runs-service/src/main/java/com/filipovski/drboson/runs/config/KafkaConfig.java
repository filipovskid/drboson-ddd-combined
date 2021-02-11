package com.filipovski.drboson.runs.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:messaging/messaging.properties")
public class KafkaConfig {

    @Value("${kafka.datasets.topic}")
    private String datasetsTopic;

    @Value("${kafka.runs.topic}")
    private String runsTopic;
}
