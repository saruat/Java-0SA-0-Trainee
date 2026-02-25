package ru.saruat;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import ru.saruat.service.ConsoleService;

@SpringBootApplication
public class Application {
    public static void main (String [] args){
        SpringApplication.run(Application.class, args);
    }

    /*@Bean
    @ConditionalOnProperty(
            value = "app.console.enabled",
            havingValue = "true",
            matchIfMissing = false
    )
    public ApplicationRunner consoleRunner(ConsoleService consoleService) {
        return args -> {
            consoleService.runConsole(); // запускаем консольное меню
        };
    }*/
}

