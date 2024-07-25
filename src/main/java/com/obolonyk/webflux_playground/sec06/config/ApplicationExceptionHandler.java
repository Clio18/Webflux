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
import java.util.function.Consumer;

@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex, request, problem ->{
            problem.setTitle("Customer not found");
            problem.setInstance(URI.create(request.path()));
        });
    }

    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, ex, request, problem ->{
            problem.setType(URI.create("http://example.com/problems/invalid-input"));
            problem.setTitle("Invalid input");
        });
    }

    private Mono<ServerResponse> handleException(HttpStatus status,
                                                 Exception ex,
                                                 ServerRequest request,
                                                 Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setInstance(URI.create(request.path()));
        consumer.accept(problem);
        return ServerResponse.status(status).bodyValue(problem);
    }


}
