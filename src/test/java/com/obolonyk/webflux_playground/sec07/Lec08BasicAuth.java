 package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec08BasicAuth extends AbstractWebclient{
    private final WebClient webClient = createWebClient(
            h -> h.defaultHeaders(
                    b -> b.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")));

    @Test
    void bearerAuth() {
        webClient.get()
                .uri("/lec08/product/{id}", 1)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
