package com.example.appntvirtualthread.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("integration.mock.grpc")
public class GrpcMockProperties {

    private String host;
    private int port;
    private String topic;
}
