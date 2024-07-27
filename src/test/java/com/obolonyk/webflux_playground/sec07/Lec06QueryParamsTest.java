package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec06QueryParamsTest extends AbstractWebclient{
    private final WebClient webClient = createWebClient();

    @Test
    void queryParams() {
        var path = "/lec06/calculator";
        var param = "first={first}&second={second}&operation={operator}";
        webClient.get()
                .uri(
                        builder ->
                                builder.path(path).query(param).build(10, 20 , "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void queryParamsMap() {
        var path = "/lec06/calculator";
        var param = "first={first}&second={second}&operation={operator}";
        var map = Map.of(
                "first", 10,
                "second", 20,
                "operator", "*"
        );
        webClient.get()
                .uri(
                        builder ->
                                builder.path(path).query(param).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
