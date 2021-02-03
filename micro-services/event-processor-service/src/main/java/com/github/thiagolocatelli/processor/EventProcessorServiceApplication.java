package com.github.thiagolocatelli.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventProcessorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventProcessorServiceApplication.class, args);
    }

}
