package com.example.appntplatformthread.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class SseHttpClient {

    private final String mapping;

    public SseHttpClient(@Value("${integration.mock.url}") String mapping) {
        this.mapping = mapping;
    }

    @SuppressWarnings("deprecation")
    public String readSseStream(Integer length, Integer latency, Long timeout) {
        StringBuilder result = new StringBuilder();
        Map<String, String> params = new HashMap<>();
        params.put("length", String.valueOf(length));
        params.put("latency", String.valueOf(latency));
        params.put("timeout", String.valueOf(timeout));
        try {
            String mapping = this.mapping + "/stream" + addQueryParam(params);
            URL url = new URL(mapping);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "text/event-stream");
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

    private String addQueryParam(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().equals("null"))
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce("?", (a, b) -> a + "&" + b);
    }

}
