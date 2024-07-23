package com.obolonyk.webflux_playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("traditional")
public class TraditionalWebController {
    private static final Logger log = LoggerFactory.getLogger(TraditionalWebController.class);

    //for traditional sync programming
    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

    @GetMapping("/products")
    public List<Product> getProducts() {
        var products = this.restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        //The data will be printed only after all products are collected
        //even if request cancels, the data will be collected
        log.info("Products: {}", products);
        return products;
    }

}
