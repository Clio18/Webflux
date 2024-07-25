package com.obolonyk.webflux_playground.sec06.exception;

import reactor.core.publisher.Mono;

// factory class for creating exceptions
public class ApplicationException {
    public static <T> Mono<T> customerNotFound(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName() {
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> missingValidEmail() {
        return Mono.error(new InvalidInputException("A valid email is required"));
    }

}
