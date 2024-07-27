package com.obolonyk.webflux_playground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
}
