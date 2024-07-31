package com.obolonyk.webflux_playground.sec10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;


public class Lec02Http2Test extends AbstractWebclient {
    private final WebClient client = createWebClient(b -> {
        // for HTTP2 only one connection will be established,
        // however we need to ensure the our Loadbalancer supports HTTP2!
        var poolSize = 1;
        var provider = ConnectionProvider.builder("my")
                .lifo()
                .maxConnections(poolSize)
                .build();

        var httpClient = HttpClient.create(provider)
                // we have no security certificate so use H2C
                .protocol(HttpProtocol.H2C)
                .compress(true)
                .keepAlive(true);

        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    void concurrentRequest() {
        var max = 10_000;
        Flux.range(1, max)
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
