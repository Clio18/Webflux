package com.obolonyk.webflux_playground.sec06.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.BiFunction;

@Configuration
public class Calculator {
    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes() {
        return RouterFunctions.route()
                .GET("/calculator/{a}/{b}", isOperator("+"), handle(Integer::sum))
                .GET("/calculator/{a}/{b}", isOperator("-"), handle((a, b) -> a-b))
                .GET("/calculator/{a}/{b}", isOperator("*"), handle((a, b) -> a*b))
                .GET("/calculator/{a}/{b}", isOperator("/"), handle((a, b) -> a/b))
                .GET("/calculator/{a}/{b}", handle("Operator not supported. It should be like: + - * /"))
                .GET("/calculator/{a}/0", isOperator("/"), handle("Division by zero is not allowed"))
                .build();
    }

    private RequestPredicate isOperator(String operator) {
        return RequestPredicates.headers(headers -> headers.header("operator").getFirst().equals(operator));
    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer, Integer, Integer> func) {
        return req -> {
            int a = Integer.parseInt(req.pathVariable("a"));
            int b = Integer.parseInt(req.pathVariable("b"));
            return ServerResponse.ok().bodyValue(func.apply(a, b));
        };
    }

    private HandlerFunction<ServerResponse> handle(String message) {
        return req -> ServerResponse.badRequest().bodyValue(message);
    }
}
