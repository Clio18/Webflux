package com.obolonyk.webflux_playground.sec02;

import com.obolonyk.webflux_playground.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec02ProductRepositoryTest extends AbstractTest{
    public static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findProductsByPriceRange(){
        productRepository.findProductsByPriceInRange(200, 400)
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }

    @Test
    void findProductsByBetween(){
        productRepository.findByPriceBetween(200, 400)
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }
}
