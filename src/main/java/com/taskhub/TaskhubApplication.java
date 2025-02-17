package com.taskhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TaskhubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskhubApplication.class, args);
    }
}
