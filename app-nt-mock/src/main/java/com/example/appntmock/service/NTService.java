package com.example.appntmock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class NTService {

    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    public String test(int latency) {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return textGenerator(100);
    }

    public SseEmitter stream(int length, int latency, long timeout) {
        SseEmitter sseEmitter = new SseEmitter(timeout);
        Thread.startVirtualThread(() -> {
            try {
                for (char c : textGenerator(length).toCharArray()) {
                    sendToSSE(sseEmitter, c);
                    sleep(latency);
                }
                sendToSSE(sseEmitter, "[DONE]");
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

    public Flux<String> stream2(int length, int latency) {
        List<String> text = new ArrayList<>();
        for (char c : textGenerator(length).toCharArray()) {
            text.add(String.valueOf(c));
        }
        return Flux.fromIterable(text)
                .doOnNext(log::info)
                .delayElements(Duration.ofMillis(latency))
                .take(Duration.ofSeconds(1));
    }

    private String textGenerator(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sleep(long latency) throws InterruptedException {
        if (latency > 0) {
            Thread.sleep(latency);
        }
    }

    private void sendToSSE(SseEmitter sseEmitter, Object object) throws IOException {
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .data(String.valueOf(object));
        sseEmitter.send(event);
        log.info("SSE -> {}", object);
    }
}
