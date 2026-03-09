package ru.saruat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.saruat.dto.UserEvent;

@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String userEventsTopic;

    public KafkaSenderService(KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${kafka.topics.userEvents}") String userEventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.userEventsTopic = userEventsTopic;
    }

    public void sendUserEvent(UserEvent event){
        kafkaTemplate.send(userEventsTopic, event);
    }

}
