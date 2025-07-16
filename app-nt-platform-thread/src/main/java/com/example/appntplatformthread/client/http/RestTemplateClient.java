package com.example.appntplatformthread.client.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * По умолчанию использует HttpURLConnection
 */
@Component
public class RestTemplateClient implements HttpClient {

    @Value("${integration.mock.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String mockIntegration() {
        return restTemplate.getForObject(url + "/mock", String.class);
    }

}
