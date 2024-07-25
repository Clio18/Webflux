package com.obolonyk.webflux_playground.sec06.config;

import com.obolonyk.webflux_playground.sec06.exception.CustomerNotFoundException;
import com.obolonyk.webflux_playground.sec06.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleCustomerNotFound(CustomerNotFoundException ex, ServerRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
        problem.setTitle("Customer not found");
        problem.setInstance(URI.create(request.path()));
        return ServerResponse.status(status).bodyValue(problem);
    }

    public Mono<ServerResponse> handleInvalidInput(InvalidInputException ex, ServerRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/invalid-input"));
        problem.setTitle("Invalid input");
        problem.setInstance(URI.create(request.path()));
        return ServerResponse.status(status).bodyValue(problem);
    }


}
