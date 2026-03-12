package ru.saruat.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationClient {

    private final WebClient webClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public NotificationClient(WebClient webClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = webClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String sendNotification(String message) {
        CircuitBreaker cb = circuitBreakerFactory.create("notificationCB");

        return cb.run(
                () -> webClient.post()
                        .uri("http://notification-service/api/notify")
                        .bodyValue(message)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(),
                throwable -> "Circuit open: Notification service unavailable"
        );
    }
}