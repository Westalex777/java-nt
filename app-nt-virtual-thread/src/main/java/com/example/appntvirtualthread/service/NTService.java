package com.example.appntvirtualthread.service;

import com.example.appntvirtualthread.client.SseHttpClient;
import com.example.appntvirtualthread.client.http.HttpClientDispatcher;
import com.example.appntvirtualthread.config.GrpcMockProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class NTService {

    private final HttpClientDispatcher httpClientDispatcher;
    private final SseHttpClient sseHttpClient;
    private final GrpcMockProperties grpcMockProperties;
    private ManagedChannel channel;
    private TextStreamerGrpc.TextStreamerStub stub;
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    public String test(String httpClientName) {
        var httpClient = httpClientDispatcher.apply(httpClientName);
        return httpClient.mockIntegration();
    }

    public String test(Long latency) {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return textGenerator(100);
    }

    public String stream(Integer length, Long latency, Long timeout) {
        return sseHttpClient.readSseStream(length, latency, timeout);
    }

    public void grpcStream() {
        channel = ManagedChannelBuilder.forAddress(grpcMockProperties.getHost(), grpcMockProperties.getPort())
                .usePlaintext()
                .build();
        stub = TextStreamerGrpc.newStub(channel);

        startStreaming(grpcMockProperties.getTopic());
    }

    private String textGenerator(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void startStreaming(String topic) {
        TextRequest request = TextRequest.newBuilder()
                .setTopic(topic)
                .build();

        stub.streamText(request, new StreamObserver<>() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void onNext(TextResponse response) {
                stringBuilder.append(response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("gRPC error: " + t.getMessage());
                shutdown();
            }

            @Override
            public void onCompleted() {
                stringBuilder = new StringBuilder();
                shutdown();
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }

}
