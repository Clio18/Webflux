package com.obolonyk.webflux_playground.sec10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01HttpConnectionPoolingTest extends AbstractWebclient{
    private final WebClient client = createWebClient();

    @Test
    void concurrentRequest(){
        var max = 1;
        Flux.range(1, max)
                .flatMap(id -> getProduct(id))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> Assertions.assertEquals(max, list.size()))
                .expectComplete()
                .verify();  
    }

    private Mono<ProductDto> getProduct(Integer id){
        return client.get()
                .uri("/product/{id}", 1)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }

}
