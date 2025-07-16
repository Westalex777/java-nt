package org.example.appntloader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NTLoaderService {

    private final String url;
    private final RestTemplate restTemplate = new RestTemplate();

    public NTLoaderService(@Value("${integration.service.url}") String url) {
        this.url = url + "/test";
    }

    public String load(double k) throws ExecutionException, InterruptedException {
        @SuppressWarnings("unchecked") CompletableFuture<String>[] array = (CompletableFuture<String>[]) new CompletableFuture[(int) (k * 1)];
        for (int i = 0; i < array.length; i++) {
            array[i] = integration();
        }

        return CompletableFuture.allOf(array)
                .thenApply(v -> {
                    StringBuilder builder = new StringBuilder();
                    for (CompletableFuture<String>  future : array) {
                        builder.append(future.join());
                    }
                    return "Results: " + builder;
                }).get();
    }

    private CompletableFuture<String> integration() {
        return CompletableFuture.supplyAsync(
                () -> restTemplate.getForObject(url, String.class),
                Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())
        );
    }

    public static void main(String[] args) {
        double k = 0.1;
        int a = (int) (115 * k);
        System.out.println(a);
    }
}
