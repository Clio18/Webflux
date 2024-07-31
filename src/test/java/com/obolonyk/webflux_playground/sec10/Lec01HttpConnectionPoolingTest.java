package com.obolonyk.webflux_playground.sec10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec01HttpConnectionPoolingTest extends AbstractWebclient {
    private final WebClient client = createWebClient(b -> {
        var poolSize = 501;
        // any name
        var provider = ConnectionProvider.builder("my")
                // queue to reuse connection
                .lifo()
                .maxConnections(poolSize)
                // the size of the queue where requests will be placed and wait for connection
                .pendingAcquireMaxCount(poolSize * 5)
                .build();

        var httpClient = HttpClient.create(provider)
                // adjustment we need to add
                .compress(true)
                .keepAlive(true);

        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    void concurrentRequest() {
        var max = 501;
        Flux.range(1, max)
                // for max = 500 - the execution takes 5 sec
                // for max = 501 - 10 sec, because of the WebClent who can handle 500 requests by default

                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> Assertions.assertEquals(max, list.size()))
                .expectComplete()
                .verify();
    }

    private Mono<ProductDto> getProduct(Integer id) {
        return client.get()
                .uri("/product/{id}", 1)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }

}
