package com.example.appntwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NTService {

    private final WebClient webClient;

    public Mono<String> test() {
        Mono<String> request1 = webClient.get()
                .uri("/mock")
                .retrieve()
                .bodyToMono(String.class);

        Mono<String> request2 = webClient.get()
                .uri("/mock")
                .retrieve()
                .bodyToMono(String.class);

        Mono<String> request3 = webClient.get()
                .uri("/mock")
                .retrieve()
                .bodyToMono(String.class);

        // Выполнит все три запроса параллельно и соберет результат, когда все завершатся
        return Mono.zip(request1, request2, request3)
                .map(tuple3 -> {
                    String r1 = tuple3.getT1();
                    String r2 = tuple3.getT2();
                    String r3 = tuple3.getT3();
                    return "Results:\n" + r1 + "\n" + r2 + "\n" + r3;
                });
    }

}
