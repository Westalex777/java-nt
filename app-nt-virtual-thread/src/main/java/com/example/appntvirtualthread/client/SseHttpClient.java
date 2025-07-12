package com.example.appntvirtualthread.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class SseHttpClient {

    @Value("${integration.mock.url}")
    private String mapping;

    public String readSseStream(Integer length, Integer latency, Long timeout) {
        StringBuilder result = new StringBuilder();
        try {
            HttpURLConnection connection = getHttpConnection(length, latency, timeout);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data:")) {
                        result.append(line.substring(5).trim());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    @SuppressWarnings("deprecation")
    private HttpURLConnection getHttpConnection(Integer length, Integer latency, Long timeout) throws IOException {
        String mapping = this.mapping + "/stream" + addQueryParam(getParams(length, latency, timeout));
        URL url = new URL(mapping);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/event-stream");
        return connection;
    }

    private Map<String, String> getParams(Integer length, Integer latency, Long timeout) {
        Map<String, String> params = new HashMap<>();
        params.put("length", String.valueOf(length));
        params.put("latency", String.valueOf(latency));
        params.put("timeout", String.valueOf(timeout));
        return params;
    }

    private String addQueryParam(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .filter(e -> !e.getValue().equals("null"))
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce("?", (a, b) -> a + "&" + b);
    }

}
