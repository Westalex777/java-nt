package com.example.appntplatformthread.controller;

import com.example.appntplatformthread.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NTController {

    private final NTService service;

    @GetMapping("/test")
    public CompletableFuture<String> test() {
        log.info("Request /test");
        return service.test();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String stream(
            @RequestParam(name = "length", required = false) Integer length,
            @RequestParam(name = "latency", required = false) Integer latency,
            @RequestParam(name = "timeout", required = false) Long timeout
    ) {
        log.info("Request /stream");
        return service.stream(length, latency, timeout);
    }
}
