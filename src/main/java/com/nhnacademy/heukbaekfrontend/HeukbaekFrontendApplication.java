package com.nhnacademy.heukbaekfrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HeukbaekFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeukbaekFrontendApplication.class, args);
    }
}
