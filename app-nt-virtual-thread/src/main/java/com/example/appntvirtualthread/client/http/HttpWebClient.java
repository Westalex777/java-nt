package com.example.appntvirtualthread.client.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HttpWebClient implements HttpClient {

    @Value("${integration.mock.url}")
    private String url;

    private final WebClient webClient = WebClient.create();

    @Override
    public String mockIntegration() {
        return webClient.get()
                .uri(url + "/mock")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
