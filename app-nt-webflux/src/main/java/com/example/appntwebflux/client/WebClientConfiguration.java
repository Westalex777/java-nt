package com.example.appntwebflux.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${integration.mock.url}")
    private String url;

    @Bean
    public WebClient webClient() {
        return WebClient.create(url);
    }
}
