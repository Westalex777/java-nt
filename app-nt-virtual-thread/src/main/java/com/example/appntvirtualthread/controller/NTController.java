package com.example.appntvirtualthread.controller;

import com.example.appntvirtualthread.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NTController {

    private final NTService service;

    @GetMapping("/test")
    public String test(@RequestParam(name = "httpClient", required = false, defaultValue = "httpClientImpl") String httpClient) {
        return service.test(httpClient);
    }

    @GetMapping("/mock")
    public String test(@RequestParam(name = "latency", required = false) Long latency) {
        return service.test(latency);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String stream(
            @RequestParam(name = "length", required = false) Integer length,
            @RequestParam(name = "latency", required = false) Long latency,
            @RequestParam(name = "timeout", required = false) Long timeout
    ) {
        return service.stream(length, latency, timeout);
    }

    @GetMapping("/grpc/stream")
    public void grpcStream() {
        service.grpcStream();
    }
}
