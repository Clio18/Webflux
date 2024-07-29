package com.obolonyk.webflux_playground.sec08;


import com.obolonyk.webflux_playground.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;


public class ProductUploadTest {
    public static final Logger log = LoggerFactory.getLogger(ProductUploadTest.class);
    private final ProductClient client = new ProductClient();

    @Test
    void upload(){
        var flux = Flux.range(1, 1_000 )
                .map(i -> new ProductDto(null, "product" + i, i * 100));

        client.upload(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void download(){
        client.download()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
