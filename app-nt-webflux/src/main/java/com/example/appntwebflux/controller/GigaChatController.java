package com.example.appntwebflux.controller;

import com.example.appntwebflux.service.GigaChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/giga")
@RequiredArgsConstructor
public class GigaChatController {

    private final GigaChatService gigaChatService;

    @GetMapping("/stream")
    public Flux<String> stream() {
        return gigaChatService.stream();
    }

}
