package ru.saruat;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.saruat.service.ConsoleService;

@SpringBootApplication
public class Application {
    public static void main (String [] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner consoleRunner(ConsoleService consoleService) {
        return args -> {
            consoleService.runConsole(); // запускаем консольное меню
        };
    }
}

