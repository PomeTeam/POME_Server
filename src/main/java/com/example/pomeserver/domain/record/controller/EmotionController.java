package com.example.pomeserver.domain.record.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/emotions")
@RestController
@Api(tags = "Emotion 관련 API")
public class EmotionController {

}
