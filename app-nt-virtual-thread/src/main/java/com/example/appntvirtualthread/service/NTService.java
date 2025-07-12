package com.example.appntvirtualthread.service;

import com.example.appntvirtualthread.client.SseHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NTService {

    private final String url;
    private final RestTemplate restTemplate = new RestTemplate();
    private final SseHttpClient sseHttpClient;

    public NTService(@Value("${integration.mock.url}") String url, SseHttpClient sseHttpClient) {
        this.url = url + "/mock";
        this.sseHttpClient = sseHttpClient;
    }

    public CompletableFuture<String> test() {
        CompletableFuture<String> request1 = mockIntegration();
        CompletableFuture<String> request2 = mockIntegration();
        CompletableFuture<String> request3 = mockIntegration();

        return CompletableFuture.allOf(request1, request2, request3)
                .thenApply(v -> {
                    String r1 = request1.join();
                    String r2 = request2.join();
                    String r3 = request3.join();
                    return "Results:\n" + r1 + "\n" + r2 + "\n" + r3;
                });
    }

    public String stream(Integer length, Integer latency, Long timeout) {
        return sseHttpClient.readSseStream(length, latency, timeout);
    }

    private CompletableFuture<String> mockIntegration() {
        return CompletableFuture.supplyAsync(
                () -> restTemplate.getForObject(url, String.class),
                Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())
        );
    }

}
