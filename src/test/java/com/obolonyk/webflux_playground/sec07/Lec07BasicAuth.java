package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec07BasicAuth extends AbstractWebclient{
    private final WebClient webClient = createWebClient(
            h -> h.defaultHeaders(
                    b -> b.setBasicAuth("java", "secret")));

    @Test
    void basicAuth() {
        webClient.get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
