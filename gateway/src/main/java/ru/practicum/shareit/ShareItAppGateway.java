package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ShareItAppGateway {
    public static void main(String[] args) {
        SpringApplication.run(ShareItAppGateway.class, args);
    }

}
