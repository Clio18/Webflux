package com.obolonyk.webflux_playground.sec08.service;

import com.obolonyk.webflux_playground.sec08.dto.ProductDto;
import com.obolonyk.webflux_playground.sec08.mapper.EntityDtoMapper;
import com.obolonyk.webflux_playground.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> saveAll(Flux<ProductDto> flux){
        return flux.map(EntityDtoMapper::dtoToEntity)
                .as(productRepository::saveAll)
                .map(EntityDtoMapper::entityToDto);
    }

    public Mono<Long> getCount(){
         return productRepository.count();
    }

}
