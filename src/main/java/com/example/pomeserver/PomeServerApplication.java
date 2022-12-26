package com.example.pomeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class PomeServerApplication {

//    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "classpath:application.yml,"
//            + "src/main/resources/real-application.yml";

    public static void main(String[] args) {
//        new SpringApplicationBuilder(PomeServerApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                        .run(args);
        SpringApplication.run(PomeServerApplication.class, args);
    }
}
