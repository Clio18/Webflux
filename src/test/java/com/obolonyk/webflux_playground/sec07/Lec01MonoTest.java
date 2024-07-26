package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebclient {
    private final WebClient webClient = createWebClient();

    @Test
    void getProduct() throws InterruptedException {
        webClient.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .subscribe();

        //we should block thread because result is async
        Thread.sleep(Duration.ofSeconds(2));
    }
    @Test
    void concurrent() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            webClient.get()
                    .uri("/lec01/product/" + i)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .doOnNext(print())
                    .subscribe();
        }

        //we should block thread because result is async
        Thread.sleep(Duration.ofSeconds(2));
    }
}
