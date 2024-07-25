package com.obolonyk.webflux_playground.sec06.config;

import com.obolonyk.webflux_playground.sec06.exception.CustomerNotFoundException;
import com.obolonyk.webflux_playground.sec06.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Autowired
    private CustomerRequestHandler handler;
    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .GET("/customers", handler::allCustomers)
                .GET("/customers/{id}", handler::getCustomerById)
                .GET("/customers/paginated", handler::allCustomersPageable)
                .POST("/customers", handler::saveCustomer)
                .PUT("/customers/{id}", handler::updateCustomer)
                .DELETE("/customers/{id}", handler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }
}
