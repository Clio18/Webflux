package com.obolonyk.webflux_playground.sec04;

import com.obolonyk.webflux_playground.sec04.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = "sec=sec04")
@AutoConfigureWebTestClient
public class Lec04CustomerServiceTest {
    @Autowired
    private WebTestClient client;
    public static final Logger log = LoggerFactory.getLogger(Lec04CustomerServiceTest.class);

    @Test
    void getAll() {
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(CustomerDto.class)
                .hasSize(10);
    }

    @Test
    void getAllPageable() {
        client.get()
                .uri("/customers/paginated?page=1&size=5")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(CustomerDto.class)
                .hasSize(5);
    }

    @Test
    void getByIdSuccess() {
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam");
    }

    @Test
    void getByIdNotSuccess() {
        client.get()
                .uri("/customers/100")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/customer-not-found")
                .jsonPath("$.title").isEqualTo("Customer not found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.detail").isEqualTo("Customer [id=100] not found")
                .jsonPath("$.instance").isEqualTo("/customers/100");
    }

    @Test
    void saveSuccess() {
        var dto = new CustomerDto(null, "tom", "tom@gmail");
        client.post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("tom");

        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void saveNotSuccessNameAbsent() {
        var dto = new CustomerDto(null, null,"tom@gmail");
        client.post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("Name is required")
                .jsonPath("$.instance").isEqualTo("/customers");
    }

    @Test
    void saveNotSuccessEmailAbsent() {
        var dto = new CustomerDto(null, "tom",null);
        client.post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("A valid email is required")
                .jsonPath("$.instance").isEqualTo("/customers");
    }

    @Test
    void saveNotSuccessEmailInvalid() {
        var dto = new CustomerDto(null, "tom","tomgmail");
        client.post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("A valid email is required")
                .jsonPath("$.instance").isEqualTo("/customers");
    }

    @Test
    void updateSuccess() {
        var dto = new CustomerDto(null, "tom", "tom@gmail");
        client.put()
                .uri("/customers/1")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("tom");
    }

    @Test
    void updateNotSuccessNameAbsent() {
        var dto = new CustomerDto(null, null,"tom@gmail");
        client.put()
                .uri("/customers/1")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("Name is required")
                .jsonPath("$.instance").isEqualTo("/customers/1");
    }

    @Test
    void updateNotSuccessEmailAbsent() {
        var dto = new CustomerDto(null, "tom",null);
        client.put()
                .uri("/customers/1")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("A valid email is required")
                .jsonPath("$.instance").isEqualTo("/customers/1");
    }

    @Test
    void updateNotSuccessEmailInvalid() {
        var dto = new CustomerDto(null, "tom","tomgmail");
        client.put()
                .uri("/customers/1")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/invalid-input")
                .jsonPath("$.title").isEqualTo("Invalid input")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.detail").isEqualTo("A valid email is required")
                .jsonPath("$.instance").isEqualTo("/customers/1");
    }

    @Test
    void updateNotSuccessNotFound() {
        var dto = new CustomerDto(null, "tom","tom@gmail");
        client.put()
                .uri("/customers/100")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/customer-not-found")
                .jsonPath("$.title").isEqualTo("Customer not found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.detail").isEqualTo("Customer [id=100] not found")
                .jsonPath("$.instance").isEqualTo("/customers/100");
    }

    @Test
    void deleteNotSuccessNotFound() {
        client.delete()
                .uri("/customers/100")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.type").isEqualTo("http://example.com/problems/customer-not-found")
                .jsonPath("$.title").isEqualTo("Customer not found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.detail").isEqualTo("Customer [id=100] not found")
                .jsonPath("$.instance").isEqualTo("/customers/100");
    }
}
