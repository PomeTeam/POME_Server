package com.example.pomeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class PomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PomeServerApplication.class, args);
    }
}
