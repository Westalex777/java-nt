package com.example.appntplatformthread.service;

import com.example.appntplatformthread.client.SseHttpClient;
import com.example.appntplatformthread.client.http.HttpClientDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class NTService {

    private final HttpClientDispatcher httpClientDispatcher;
    private final SseHttpClient sseHttpClient;
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    public String test(String httpClientName) {
        var httpClient = httpClientDispatcher.apply(httpClientName);
        return httpClient.mockIntegration();
    }

    public String test(int latency) {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return textGenerator(100);
    }

    public String stream(Integer length, Integer latency, Long timeout) {
        return sseHttpClient.readSseStream(length, latency, timeout);
    }

    private String textGenerator(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

}
