package com.example.appntplatformthread.client.http;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class HttpClientDispatcher implements Function<String, HttpClient> {

    private final Map<String, HttpClient> clients;

    @Override
    public HttpClient apply(String s) {
        return clients.get(s);
    }
}
