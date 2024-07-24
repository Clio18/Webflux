package com.obolonyk.webflux_playground.sec03;

import com.obolonyk.webflux_playground.sec03.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec03")
public class CustomerServiceTest {
    public static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);
    @Autowired
    private WebTestClient client;

    @Test
    void allCustomers(){
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(customers -> log.info("{}", customers))
                .hasSize(10);
    }

    @Test
    void paginatedCustomers(){
        client.get()
                .uri("/customers/paginated?page=1&size=3")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].name").isEqualTo("sam")
                .jsonPath("$[1].name").isEqualTo("mike")
                .jsonPath("$[2].name").isEqualTo("jake");
    }

    @Test
    void getCustomerById(){
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.name").isEqualTo("sam");
    }

    @Test
    void createAndDelete(){
        //create
        CustomerDto dto = new CustomerDto(null, "tom", "tom@gmail.com");
        client.post()
                .uri("/customers")
                // if you have raw object - use bodyValue, if publisher type like Mono or Flux - use body
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("tom")
                .jsonPath("$.email").isEqualTo("tom@gmail.com");
        //delete
        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    void update() {
        CustomerDto dto = new CustomerDto(null, "tom", "tom@gmail.com");
        client.put()
                .uri("/customers/10")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("tom")
                .jsonPath("$.email").isEqualTo("tom@gmail.com");
    }

    @Test
    void notFound(){
        client.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        CustomerDto dto = new CustomerDto(null, "tom", "tom@gmail.com");
        client.put()
                .uri("/customers/11")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();
    }
}
