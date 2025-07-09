package com.example.appntmock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class NTService {

    private final AtomicInteger counter = new AtomicInteger(0);

    public int test() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}
