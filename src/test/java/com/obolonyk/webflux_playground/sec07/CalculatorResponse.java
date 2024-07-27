package com.obolonyk.webflux_playground.sec07;

public record CalculatorResponse(
        Integer first,
        Integer second,
        String operation,
        Double result
) {
}
