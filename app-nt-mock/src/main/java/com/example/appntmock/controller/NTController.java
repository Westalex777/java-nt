package com.example.appntmock.controller;

import com.example.appntmock.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NTController {

    private final NTService service;

    @GetMapping("/mock")
    public String test(@RequestParam(name = "latency", defaultValue = "1000", required = false) int latency) {
        log.info("Request /mock");
        return service.test(latency);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestParam(name = "length", defaultValue = "100", required = false) int length,
            @RequestParam(name = "latency", defaultValue = "100", required = false) int latency,
            @RequestParam(name = "timeout", defaultValue = "10000", required = false) long timeout
    ) {
        log.info("Request /stream");
        return service.stream(length, latency, timeout);
    }

    @GetMapping(value = "/stream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream2(
            @RequestParam(name = "length", defaultValue = "100", required = false) int length,
            @RequestParam(name = "latency", defaultValue = "100", required = false) int latency,
            @RequestParam(name = "timeout", defaultValue = "10000", required = false) long timeout
    ) {
        log.info("Request /stream2");
        return service.stream2(length, latency, timeout);
    }

}
