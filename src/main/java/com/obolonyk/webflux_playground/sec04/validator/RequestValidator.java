package com.obolonyk.webflux_playground.sec04.validator;

import com.obolonyk.webflux_playground.sec04.dto.CustomerDto;
import com.obolonyk.webflux_playground.sec04.exception.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate(){
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationException.missingName())
                .filter(hasValidEmail())
                .switchIfEmpty(ApplicationException.missingValidEmail());
    }

    private static Predicate<CustomerDto> hasName(){
        return c -> Objects.nonNull(c.name());
    }

    private static Predicate<CustomerDto> hasValidEmail(){
        return c -> Objects.nonNull(c.email()) && c.email().contains("@");
    }
}
