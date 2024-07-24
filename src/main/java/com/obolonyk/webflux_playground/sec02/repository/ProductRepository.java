package com.obolonyk.webflux_playground.sec02.repository;

import com.obolonyk.webflux_playground.sec02.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    @Query("SELECT * FROM product WHERE price BETWEEN :min AND :max")
    Flux<Product> findProductsByPriceInRange(Integer min, Integer max);
    Flux<Product> findByPriceBetween(Integer min, Integer max);
}
