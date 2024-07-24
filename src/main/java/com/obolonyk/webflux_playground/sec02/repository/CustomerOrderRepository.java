package com.obolonyk.webflux_playground.sec02.repository;

import com.obolonyk.webflux_playground.sec02.entity.CustomerOrder;
import com.obolonyk.webflux_playground.sec02.entity.Product;
import com.obolonyk.webflux_playground.sec02.dto.OrderInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, Integer>{

    @Query("""
            select p.* from product p
            join customer_order co on p.id = co.product_id
            join customer c on c.id = co.customer_id
            where c.name = :name
            """)
    Flux<Product> getProductByCustomerName(String name);


    @Query("""
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
        """
    )
    Flux<OrderInfo> getOrderInfo(String description);
}
