package com.obolonyk.webflux_playground.sec06.config;

import com.obolonyk.webflux_playground.sec06.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Autowired
    private CustomerRequestHandler customerRequestHandler;
    @Bean
    public RouterFunction<ServerResponse> customerRoutes(){
        return RouterFunctions.route()
                .GET("/customers", customerRequestHandler::allCustomers)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)
                .POST("/customers", customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, (ex, request) -> ServerResponse.notFound().build())
                .build();
    }
}
