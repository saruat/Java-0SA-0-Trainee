package ru.saruat.notification_service;

import org.springframework.http.ResponseEntity;
import ru.saruat.dto.UserEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.awaitility.Awaitility;


import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class NotificationServiceIntegrationTest {

    static final GenericContainer<?> mailServer = new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(1025, 8025);

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.0.1"));

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailServer::getHost); // getHost() — IP хоста
        registry.add("spring.mail.port", () -> mailServer.getMappedPort(1025));
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    @Test
    void shouldSendEmailViaKafka() {
        UserEvent event = new UserEvent("CREATE", "kafka@test.com");
        kafkaTemplate.send("user-events", event);

        waitForEmail("kafka@test.com", "Добро пожаловать!");
    }

    @Test
    void shouldSendEmailViaApi() {
        UserEvent event = new UserEvent("DELETE", "api@test.com");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/notify", event, String.class);

        assertEquals(200, response.getStatusCodeValue());
        waitForEmail("api@test.com", "Аккаунт удалён");
    }

    private void waitForEmail(String to, String subject) {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            String url = String.format("http://%s:%d/api/v2/messages",
                    mailServer.getHost(), mailServer.getMappedPort(8025));
            String messages = restTemplate.getForObject(url, String.class);
            assertEquals(true, messages.contains(to));
            assertEquals(true, messages.contains(subject));
        });
    }
}