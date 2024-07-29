package com.obolonyk.webflux_playground.sec02;

import com.obolonyk.webflux_playground.sec02.entity.Customer;
import com.obolonyk.webflux_playground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec02CustomerRepositoryTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findAll() {
        customerRepository.findAll()
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        customerRepository.findById(2)
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByName() {
        customerRepository.findCustomersByName("mike")
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                //.expectNextCount(1)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByEmailEndWith() {
        customerRepository.findCustomersByEmailEndsWith("ke@gmail.com")
                .doOnNext(c -> log.info("Customer: {}", c))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void insertAndDelete() {
        //insert first
        Customer c = new Customer();
        c.setName("test");
        c.setEmail("test@test");
        customerRepository.save(c)
                .doOnNext(cust -> log.info("Inserted customer: {}", cust))
                .as(StepVerifier::create)
                .assertNext(cust -> {
                    Assertions.assertNotNull(cust.getId());
                })
                .expectComplete()
                .verify();

        //check count
        customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        //delete and check count
        customerRepository.deleteById(11)
                .then(customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    void updateName() {
        customerRepository.findCustomersByName("mike")
                .doOnNext(c -> c.setName("michael"))
                //map will return Mono<Customer> so use flatMap instead
                .flatMap(c -> customerRepository.save(c))
                .doOnNext(c -> log.info("Updated customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

}
