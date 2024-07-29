package com.obolonyk.webflux_playground.sec09.service;

import com.obolonyk.webflux_playground.sec09.dto.ProductDto;
import com.obolonyk.webflux_playground.sec09.mapper.EntityDtoMapper;
import com.obolonyk.webflux_playground.sec09.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Sinks.Many<ProductDto> sink;

    public Mono<ProductDto> saveProduct(Mono<ProductDto> mono){
        return mono.map(EntityDtoMapper::dtoToEntity)
                .flatMap(productRepository::save )
                .map(EntityDtoMapper::entityToDto)
                // when we adding new product we sent notification to browser about the update
                .doOnNext(dto -> sink.tryEmitNext(dto));
    }

    public Flux<ProductDto> productStream(){
        return sink.asFlux();
    }

}
