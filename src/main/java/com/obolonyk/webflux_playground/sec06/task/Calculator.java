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
                .path("/calculator", this::routes)
                .build();
    }

    private RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/{a}/{b}", isOperator("+"), handle(Integer::sum))
                .GET("/{a}/{b}", isOperator("-"), handle((a, b) -> a-b))
                .GET("/{a}/{b}", isOperator("*"), handle((a, b) -> a*b))
                .GET("/{a}/{b}", isOperator("/"), handle((a, b) -> a/b))
                .GET("/{a}/{b}", badRequest("Operator not supported. It should be like: + - * /"))
                .GET("/{a}/0", isOperator("/"), badRequest("Division by zero is not allowed"))
                .build();
    }

    private RequestPredicate isOperator(String operator) {
        return RequestPredicates.headers(h -> operator.equals(h.firstHeader("operator")));
    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer, Integer, Integer> func) {
        return req -> {
            var a = Integer.parseInt(req.pathVariable("a"));
            var b = Integer.parseInt(req.pathVariable("b"));
            var result = func.apply(a, b);
            return ServerResponse.ok().bodyValue(result);
        };
    }

    private HandlerFunction<ServerResponse> badRequest(String message){
        return req -> ServerResponse.badRequest().bodyValue(message);
    }
}
