package ru.saruat.service;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.saruat.dto.UserDTO;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080";

    private final ConfigurableApplicationContext context;

    public ClientService(ConfigurableApplicationContext context) {
        this.context = context;
        this.restTemplate = new RestTemplate();
    }

    public UserDTO getUser(UUID id) {
        return restTemplate.getForObject(baseUrl + "/api/users/{id}", UserDTO.class, id);
    }

    public List<UserDTO> getAllUsers() {
        return Arrays.asList(restTemplate.getForObject(baseUrl + "/api/users", UserDTO[].class));
    }

    public UserDTO createUser(UserDTO userDTO) {
        return restTemplate.postForObject(baseUrl + "/api/users", userDTO, UserDTO.class);
    }

    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        return restTemplate.exchange(baseUrl + "/api/users/{id}", HttpMethod.PUT, new HttpEntity<>(userDTO), UserDTO.class, id).getBody();
    }

    public void deleteUser(UUID id) {
        restTemplate.delete(baseUrl + "/api/users/{id}", id);
    }

    public void stopApplication() {
        context.close();  // Остановить Spring-контекст
        System.exit(0);   // Завершить работу JVM
    }

}
