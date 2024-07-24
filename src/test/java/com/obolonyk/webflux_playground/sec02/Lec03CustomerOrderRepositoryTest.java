package com.obolonyk.webflux_playground.sec02;

import com.obolonyk.webflux_playground.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepositoryTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    void getProductByCustomerName(){
        customerOrderRepository.getProductByCustomerName("mike")
                .doOnNext(p -> log.info("Product: {}", p))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void getOrderInfo(){
        customerOrderRepository.getOrderInfo("iphone 20")
                .doOnNext(p -> log.info("Order Info: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> {
                    Assertions.assertEquals(975, p.amount());
                })
                .assertNext(p -> {
                    Assertions.assertEquals(950, p.amount());
                })
                .expectComplete()
                .verify();
    }
}
