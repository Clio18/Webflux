package com.obolonyk.webflux_playground.sec08;

import com.obolonyk.webflux_playground.sec08.dto.ProductDto;
import com.obolonyk.webflux_playground.sec08.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build();

    public Mono<UploadResponse> upload(Flux<ProductDto> flux){
        return webClient.post()
                .uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDto.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    public Flux<ProductDto> download(){
        return webClient.get()
                .uri("/products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }
}
