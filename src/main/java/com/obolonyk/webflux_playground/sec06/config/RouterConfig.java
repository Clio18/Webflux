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
    private CustomerRequestHandler customerRequestHandler;
    @Autowired
    private ApplicationExceptionHandler exceptionHandler;
    @Bean
    public RouterFunction<ServerResponse> customerRoutes(){

        //the order of routes matters, for example path "/customers/pageable" is the same as "/customers/{id}"
        return RouterFunctions.route()
                .GET("/customers", customerRequestHandler::allCustomers)

                .GET("/customers/paginated", customerRequestHandler::allCustomersPageable)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)

                .POST("/customers", customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }
}
