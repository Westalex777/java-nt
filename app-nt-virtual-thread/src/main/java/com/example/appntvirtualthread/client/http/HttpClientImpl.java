package com.example.appntvirtualthread.client.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class HttpClientImpl implements HttpClient {

    @Value("${integration.mock.url}")
    private String url;

    // HttpClient создан один раз, переиспользуется
    private final java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();

    @Override
    public String mockIntegration() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/mock"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error calling mock integration", e);
        }
    }
}
