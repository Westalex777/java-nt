package com.example.appntmock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
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
                int count = 1;
                for (char c : textGenerator(length).toCharArray()) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(LocalDateTime.now().toString())
                            .id(String.valueOf(c))
                            .name(count + " of " + length);
                    sseEmitter.send(event);
                    count++;
                    sleep(latency);
                }
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
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
}
