package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.Duration;


public class Lec01FluxTest extends AbstractWebclient{
    private final WebClient webClient = createWebClient();

    @Test
    void streaming() {
        webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(ProductDto.class)
                //gives result collected in 5 seconds
                .take(Duration.ofSeconds(5))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
