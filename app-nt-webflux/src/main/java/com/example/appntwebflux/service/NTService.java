package com.example.appntwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<String> stream(Integer latency) {
        return webClient.get()
                .uri("/stream?latency=" + latency)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

}
