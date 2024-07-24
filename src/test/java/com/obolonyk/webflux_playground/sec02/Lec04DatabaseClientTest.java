package com.obolonyk.webflux_playground.sec02;

import com.obolonyk.webflux_playground.sec02.dto.OrderInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class Lec04DatabaseClientTest extends AbstractTest{
    public static final Logger log = LoggerFactory.getLogger(Lec04DatabaseClientTest.class);

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    void getOrderInfo(){
        var query = """
                SELECT
            co.order_id,
            c.name AS customer_name,
            p.description AS product_name,
            co.amount,
            co.order_date
        FROM
            customer c
        INNER JOIN customer_order co ON c.id = co.customer_id
        INNER JOIN product p ON p.id = co.product_id
        WHERE
            p.description = :description
        ORDER BY co.amount DESC
                """;

        databaseClient.sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderInfo.class)
                .all()
                .doOnNext(p -> log.info("Order Info: {}", p))
                .as(StepVerifier::create)
                .assertNext(p -> {
                    Assertions.assertEquals(975, p.amount());
                })
                .assertNext(p -> {
                    Assertions.assertEquals(950, p.amount());
                })
                .expectComplete()
                .verify();
    }

}
