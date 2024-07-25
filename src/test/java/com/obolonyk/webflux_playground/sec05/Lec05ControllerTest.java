package com.obolonyk.webflux_playground.sec05;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = "sec=sec05")
@AutoConfigureWebTestClient
public class Lec05ControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void unauthorized(){
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void unauthorizedInvalidToken(){
        client.get()
                .uri("/customers")
                .header("auth-token", "secret")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void standardGet(){
        client.get()
                .uri("/customers")
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void standardDelete(){
        client.delete()
                .uri("/customers/1")
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void primeGet(){
        client.get()
                .uri("/customers")
                .header("auth-token", "secret456")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void primeDelete(){
        client.delete()
                .uri("/customers/1")
                .header("auth-token", "secret456")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
