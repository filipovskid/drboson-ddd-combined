package com.filipovski.drboson.authentication.integration;

import com.filipovski.drboson.authentication.domain.event.UserCreatedEvent;
import com.filipovski.drboson.sharedkernel.integration.avro.UserCreatedRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:messaging/messaging.properties")
@KafkaListener(topics = "#{'${kafka.users.topic}'}", groupId = "user-auth")
public class UserSinkService {

    private final ApplicationEventPublisher publisher;

    public UserSinkService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaHandler
    @Transactional
    public void onUserCreatedEvent(UserCreatedRecord user) {
        UserCreatedEvent event = UserCreatedEvent.from(user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );

        publisher.publishEvent(event);
    }

}
