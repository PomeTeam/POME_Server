package com.example.pomeserver.global.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class HealthCheckController {

    private Environment env;

    @Autowired
    public HealthCheckController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return Arrays.stream(env.getActiveProfiles()).findFirst().orElse("");
    }
}
