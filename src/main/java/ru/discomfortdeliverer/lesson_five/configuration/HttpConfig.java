package ru.discomfortdeliverer.lesson_five.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class HttpConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}