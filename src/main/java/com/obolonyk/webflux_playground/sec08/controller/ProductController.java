package com.obolonyk.webflux_playground.sec08.controller;

import com.obolonyk.webflux_playground.sec08.dto.ProductDto;
import com.obolonyk.webflux_playground.sec08.dto.UploadResponse;
import com.obolonyk.webflux_playground.sec08.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductService productService;

    public static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> upload(@RequestBody Flux<ProductDto> flux){
        log.info("invoked");
        return productService.saveAll(
                flux.doOnNext(dto -> log.info("received: {}", dto)))
                .then(productService.getCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> upload(){
        return productService.findAll();
    }


}
