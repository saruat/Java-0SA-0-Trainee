package ru.saruat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.saruat.dto.UserEvent;
import ru.saruat.service.EmailService;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody UserEvent event){
        switch (event.operation().toUpperCase()){
            case "CREATE" -> emailService.sendAccountCreatedEmail(event.email());
            case "DELETE" -> emailService.sendAccountDeletedEmail(event.email());
            default -> {
                return ResponseEntity.badRequest()
                        .body("Invalid operation:" + event.operation());
            }
        }
        return ResponseEntity.ok("Email sent to " + event.email());
    }
}
