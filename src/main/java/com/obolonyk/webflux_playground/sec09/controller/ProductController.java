package com.obolonyk.webflux_playground.sec09.controller;

import com.obolonyk.webflux_playground.sec09.dto.ProductDto;
import com.obolonyk.webflux_playground.sec09.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductService productService;

    public static final Logger log = LoggerFactory.getLogger(ProductController.class);

    // MediaType.APPLICATION_NDJSON_VALUE -> service to service communication
    // MediaType.TEXT_EVENT_STREAM_VALUE -> to browser (their own specification)

    @PostMapping
    public Mono<ProductDto> save(@RequestBody Mono<ProductDto> mono){
        return productService.saveProduct(mono);
    }

    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> streamProduct(@PathVariable Integer maxPrice){
        return productService.productStream()
                .filter(productDto -> productDto.price() <= maxPrice);
    }


}
