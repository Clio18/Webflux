package com.obolonyk.webflux_playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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
                // -N flag turn off the buffering and when request terminates the data will be stop collecting

                //if the page refreshes, the request will be STOPs and sent again

                //if the app from where we retrieve the data crashes, the portion of the dara will return
                // RESILIENT
                .onErrorComplete()
                        .doOnNext(product -> log.info("Product: {}", product));
    }


    // will be showing product by product without waiting for all products to be collected

    // the returning type does matter, when the browser make a request it be checking the type of the response
    // and then subscription will be created
    @GetMapping(value = "/products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProductsStream() {
        return this.webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("Product: {}", product));
    }
}
