package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec04HeaderTest extends AbstractWebclient{
    private final WebClient webClient = createWebClient(
            b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    void defaultHeader() {
        webClient.get()
                .uri("/lec04/product/{id}", 1 )
                // to override header
                //.header("caller-id", "new-value")
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
