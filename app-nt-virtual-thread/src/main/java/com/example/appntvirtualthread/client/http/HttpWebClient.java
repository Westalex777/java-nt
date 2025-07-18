package com.example.appntvirtualthread.client.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Component
public class HttpWebClient implements HttpClient {

    @Value("${integration.mock.url}")
    private String url;

    private WebClient webClient;

    @Override
    public String mockIntegration() {
        install();
        return webClient.get()
                .uri(url + "/mock")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void install() {
        if (this.webClient == null) {
            this.webClient = create();
        }
    }

    private WebClient create() {
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(2500)
                .pendingAcquireTimeout(Duration.ofSeconds(30))
                .build();

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(reactor.netty.http.client.HttpClient.create(provider)))
                .build();
    }
}
