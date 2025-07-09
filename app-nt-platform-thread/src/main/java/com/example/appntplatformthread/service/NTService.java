package com.example.appntplatformthread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NTService {

    @Value("${integration.mock.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    public CompletableFuture<String> test() {
        CompletableFuture<String> request1 = CompletableFuture.supplyAsync(() ->
                        restTemplate.getForObject(url, String.class)
        );

        CompletableFuture<String> request2 = CompletableFuture.supplyAsync(() ->
                        restTemplate.getForObject(url, String.class)
        );

        CompletableFuture<String> request3 = CompletableFuture.supplyAsync(() ->
                        restTemplate.getForObject(url, String.class)
        );

        return CompletableFuture.allOf(request1, request2, request3)
                .thenApply(v -> {
                    String r1 = request1.join();
                    String r2 = request2.join();
                    String r3 = request3.join();
                    return "Results:\n" + r1 + "\n" + r2 + "\n" + r3;
                });
    }

}
