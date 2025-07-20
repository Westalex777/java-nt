package com.example.appntwebflux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NTService {

    private final WebClient webClient;

    public Mono<String> test() {
        return webClient.get()
                .uri("/mock")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Flux<String> stream(Integer latency, Integer length) {
        return webClient.get()
                .uri(uriStreamBuilder(latency, length))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

    private String uriStreamBuilder(Integer latency, Integer length) {
        return "/stream?latency=" + latency +
                "&length=" + length;
    }

}
