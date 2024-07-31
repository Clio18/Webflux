package com.obolonyk.webflux_playground.sec10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

public abstract class AbstractWebclient {

    public static final Logger log = LoggerFactory.getLogger(AbstractWebclient.class);

    protected <T> Consumer<T> print(){
        return item -> log.info("{}", item);
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> consumer){
        var builder =  WebClient.builder()
                .baseUrl("http://localhost:8082/demo03");
        consumer.accept(builder);
        return builder.build();
    }

    protected WebClient createWebClient(){
        return createWebClient(builder -> {});
    }
}
