package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05ErrorHandling extends AbstractWebclient{
    private final WebClient webClient = createWebClient(h -> h.defaultHeader("operation", "@"));
    public static final Logger log = LoggerFactory.getLogger(Lec05ErrorHandling.class);

    @Test
    void handlingError() {

        webClient.get()
                .uri("/lec05/calculator/{first}/{second}", 10, 20)
                .retrieve()
                .bodyToMono(CalculatorResponse.class)


                .doOnError(WebClientResponseException.class, ex-> {
                    log.info("{}", ex.getResponseBodyAs(ProblemDetail.class));
                })

                //default value
                //.onErrorReturn(new CalculatorResponse(0, 0, null, 0.0))
                .onErrorReturn(
                        WebClientResponseException.InternalServerError.class,
                        new CalculatorResponse(0, 0, null, 0.0))
                .onErrorReturn(
                        WebClientResponseException.BadRequest.class,
                        new CalculatorResponse(0, 0, null, 0.0))


                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void exchanger() {
        webClient.get()
                .uri("/lec05/calculator/{first}/{second}", 10, 20)
                //exchange give as more info - full response, where we can check status or cookies
                .exchangeToMono(this::decode )
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<CalculatorResponse> decode(ClientResponse response){
        log.info("Status code {}", response.statusCode());
        if(response.statusCode().is4xxClientError()){
            return response.bodyToMono(ProblemDetail.class)
                    .doOnNext(pd -> log.info("{}", pd))
                    .then(Mono.empty());
        }
        return response.bodyToMono(CalculatorResponse.class);
    }
}
