package com.example.pomeserver.global.config;

import com.example.pomeserver.domain.record.dto.paramResolver.resolver.RecordFilteringArgumentResolver;
import com.example.pomeserver.global.util.authResolver.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    private final UserIdResolver userIdResolver;
    private final RecordFilteringArgumentResolver recordFilteringArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(recordFilteringArgumentResolver);
    }

}
