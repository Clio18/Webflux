package com.obolonyk.webflux_playground.sec08;


import com.obolonyk.webflux_playground.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ProductUploadTest {
    public static final Logger log = LoggerFactory.getLogger(ProductUploadTest.class);
    private final ProductClient client = new ProductClient();

    @Test
    void upload(){
        var flux = Flux.range(1, 10)
                .map(i -> new ProductDto(i, "product" + i, i * 100))
                .delayElements(Duration.ofSeconds(2));

        client.upload(flux)
                .doOnNext(r -> log.info("Received response: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
