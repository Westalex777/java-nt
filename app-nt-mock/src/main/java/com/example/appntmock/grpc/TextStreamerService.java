package com.example.appntmock.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Random;

@GrpcService
public class TextStreamerService extends TextStreamerGrpc.TextStreamerImplBase {

    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    @Override
    public void streamText(TextRequest request, StreamObserver<TextResponse> responseObserver) {
        String text = textGenerator(1000);
        for (char c : text.toCharArray()) {
            TextResponse response = TextResponse.newBuilder()
                    .setMessage(String.valueOf(c))
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    private String textGenerator(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sleep(long latency) {
        if (latency > 0) {
            try {
                Thread.sleep(latency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
