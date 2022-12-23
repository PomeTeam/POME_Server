package com.example.pomeserver.global.web.controller;

import com.example.pomeserver.global.config.JasyptConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HealthCheckControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JasyptConfig jasyptConfig;


    @Test
    public void Profile확인 () {
        //when
        String profile = this.restTemplate.getForObject("/health-check", String.class);
        //then
        //assertThat(profile).isEqualTo("dev");
    }



}