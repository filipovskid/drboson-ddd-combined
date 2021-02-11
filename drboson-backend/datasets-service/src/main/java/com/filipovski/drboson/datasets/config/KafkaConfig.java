package com.filipovski.drboson.datasets.config;

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

    @Value("${kafka.dataset.storage.jobs.topic}")
    private String datasetStorageJobsTopic;
}
