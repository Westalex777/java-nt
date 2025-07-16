package com.example.appntwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import reactor.netty.resources.LoopResources;

@SpringBootApplication
public class AppNtWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppNtWebfluxApplication.class, args);
    }

    @Bean
    public NettyReactiveWebServerFactory nettyFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(httpServer ->
                httpServer.runOn(LoopResources.create("my-netty", 200, true))
        );
        return factory;
    }
}
