package com.obolonyk.webflux_playground.sec09.repository;


import com.obolonyk.webflux_playground.sec09.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    @Modifying
    Mono<Boolean> deleteProductById(Integer id);

    Flux<Product> findAllBy(Pageable pageable);

}
