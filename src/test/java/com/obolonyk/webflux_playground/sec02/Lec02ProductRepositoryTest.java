package com.obolonyk.webflux_playground.sec02;

import com.obolonyk.webflux_playground.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Test
    void pageable() {
        // first 3 products: 0 - page, 3 - size
        productRepository.findBy(PageRequest.of(0, 3))
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    void pageableAndSort() {
        // first 3 products: 0 - page, 3 - size
        // sorts all, and takes first 3 -> 200, 250, 300
        productRepository.findBy(PageRequest.of(0, 3).withSort(Sort.by("price").ascending()))
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();
    }
}
