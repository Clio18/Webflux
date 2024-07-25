package com.obolonyk.webflux_playground.sec06.config;

import com.obolonyk.webflux_playground.sec06.dto.CustomerDto;
import com.obolonyk.webflux_playground.sec06.exception.ApplicationException;
import com.obolonyk.webflux_playground.sec06.entity.Customer;
import com.obolonyk.webflux_playground.sec06.service.CustomerService;
import com.obolonyk.webflux_playground.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {
    @Autowired
    private CustomerService service;

    public Mono<ServerResponse> allCustomers(ServerRequest request) {
        //return ServerResponse.ok().body(service.getAllCustomers(), Customer.class);
        return service.getAllCustomers()
                .as(flux -> ServerResponse.ok().body(flux, Customer.class));
    }

    public Mono<ServerResponse> allCustomersPageable(ServerRequest request) {
        var page = request.queryParam("page").map(Integer::valueOf).orElse(1);
        var size = request.queryParam("size").map(Integer::valueOf).orElse(3);
        return service.getAllPageable(page, size)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return service.getCustomerById(id)
                //.as(mono -> ServerResponse.ok().body(mono, Customer.class));
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(service::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validDto -> service.updateCustomer(id, validDto))
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return service.deleteCustomer(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }
}
