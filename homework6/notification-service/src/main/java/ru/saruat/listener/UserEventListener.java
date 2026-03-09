package ru.saruat.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.saruat.dto.UserEvent;
import ru.saruat.service.EmailService;

@Component
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    private final EmailService emailService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        log.info("Received Kafka event: {} for {}", event.operation(), event.email());

        switch (event.operation().toUpperCase()) {
            case "CREATE" -> emailService.sendAccountCreatedEmail(event.email());
            case "DELETE" -> emailService.sendAccountDeletedEmail(event.email());
            default -> log.warn("Unknown operation: {}", event.operation());
        }
    }
}