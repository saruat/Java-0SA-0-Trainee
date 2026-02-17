package ru.saruat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private ConfigurableApplicationContext context;

    @GetMapping("/")
    public String home() {
        return "Spring Boot приложение работает!";
    }

    @GetMapping("/stop")
    public String stopApp() {
        context.close();
        return "Приложение остановлено";
    }
}