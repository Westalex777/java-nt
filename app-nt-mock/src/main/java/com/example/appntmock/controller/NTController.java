package com.example.appntmock.controller;

import com.example.appntmock.service.NTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NTController {

    private final NTService service;

    @GetMapping("/mock")
    public String test() {
        log.info("Request /mock");
        int i = service.test();
        return LocalDateTime.now() + " -> " + i;
    }

    @GetMapping("/get")
    public int getCount() {
        return service.getCount();
    }
}
