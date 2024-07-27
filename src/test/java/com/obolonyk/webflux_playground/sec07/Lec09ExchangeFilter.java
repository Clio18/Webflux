package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec09ExchangeFilter extends AbstractWebclient {
    public static final Logger log = LoggerFactory.getLogger(Lec09ExchangeFilter.class);
    private final WebClient webClient = createWebClient(
            b ->
                    b.filter(tokenGeneration())
                            .filter(logHttpReq()));

    @Test
    void exchangeFilter() {
        webClient.get()
                .uri("/lec09/product/{id}", 1)
                .attribute("enable-logging", true)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    // to generate new token
    private ExchangeFilterFunction tokenGeneration() {
        return (req, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("Generated Token: {}", token);
            // we can not do like this because req is immutable
            // req.headers().setBearerAuth(token);

            ClientRequest newRequest = ClientRequest.from(req)
                    .headers(h -> h.setBearerAuth(token))
                    .build();
            return next.exchange(newRequest);
        };
    }

    private ExchangeFilterFunction logHttpReq() {
        return (req, next) -> {
            var isEnable = (Boolean ) req.attributes().getOrDefault("enable-logging", false);
            if (isEnable) {
                log.info("Logg info: {} {}", req.method(), req.url());
            }
            return next.exchange(req);
        };
    }

}
