package com.example.appntvirtualthread.controller;

import com.example.appntvirtualthread.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

}
