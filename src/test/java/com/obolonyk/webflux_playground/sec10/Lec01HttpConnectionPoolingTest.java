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
    void concurrentRequest() {
        var max = 260;
        Flux.range(1, max)
                // for max = 250 - the execution takes 5 sec
                // for max = 260 - 10 sec, because flatMap has it own queue for 256 capacity
                // to work in parallel way, so if max > 256 it creates 2 queues

                .flatMap(this::getProduct)
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
