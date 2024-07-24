package com.obolonyk.webflux_playground.sec02.repository;

import com.obolonyk.webflux_playground.sec02.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    Flux<Customer> findCustomersByName(String name);
    Flux<Customer> findCustomersByEmailEndsWith(String pattern);
}
