package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec04HeaderTest extends AbstractWebclient{
    private final WebClient webClient = createWebClient(
            b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    void defaultHeader() {
        var headers = Map.of(
                "caller-id", "order-service",
                "some-key", "some-value"
        );

        webClient.get()
                .uri("/lec04/product/{id}", 1 )

                // to override header
                //.header("caller-id", "new-value")
                //.headers(h ->h.setAll(headers))


                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
