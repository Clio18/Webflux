package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec03PostRequest extends AbstractWebclient{
    private final WebClient webClient = createWebClient();

    @Test
    void postBodyValue() {
        var product = new ProductDto(null, "name", 100);
        webClient.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void postBody() {
        var product = new ProductDto(null, "name", 100);
        var mono = Mono.fromSupplier(() -> product)
                .delayElement(Duration.ofSeconds(1));
        webClient.post()
                .uri("/lec03/product")
                .body(mono, ProductDto.class)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
