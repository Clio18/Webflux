package com.obolonyk.webflux_playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("reactive")
public class ReactiveWebController {
    private static final Logger log = LoggerFactory.getLogger(ReactiveWebController.class);

    //for reactive programming
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

    @GetMapping("/products")
    public Flux<Product> getProducts() {
        return this.webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                //will be immediately printed
                // -N flag turn off the buffering and when request terminates the data will be stop collecting
                        .doOnNext(product -> log.info("Product: {}", product));
    }
}
