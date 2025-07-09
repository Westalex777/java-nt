package com.example.appntwebflux.controller;

import com.example.appntwebflux.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NTController {

    private final NTService service;

    @GetMapping("/test")
    public Mono<String> test() {
        log.info("Request /test");
        return service.test();
    }

}
