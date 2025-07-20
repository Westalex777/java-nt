package com.example.appntwebflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GigaChatService {

    private final WebClient webClient;

    public Flux<String> stream() {
        return webClient.get()
                .uri("/giga/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(msg -> System.out.println("Получено сообщение: " + msg))
                .take(1)
                .doOnComplete(() -> System.out.println("Завершено после 20 сообщений"));
    }

}
