package com.obolonyk.webflux_playground.sec06.repository;


import com.obolonyk.webflux_playground.sec06.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    @Modifying
    Mono<Boolean> deleteCustomerById(Integer id);

    Flux<Customer> findAllBy(Pageable pageable);

}
