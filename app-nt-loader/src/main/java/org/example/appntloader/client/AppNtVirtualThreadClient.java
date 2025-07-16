package org.example.appntloader.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppNtVirtualThreadClient implements NTServiceClient<String> {

    @Value("${integration.apps.nt-virtual-thread.url}")
    private String url;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String integration() {
        return restTemplate.getForObject(url, String.class);
    }
}
