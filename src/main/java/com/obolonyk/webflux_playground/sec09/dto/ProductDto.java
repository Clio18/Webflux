package com.obolonyk.webflux_playground.sec09.dto;

public record ProductDto(
        Integer id,
        String description,
        Integer price) {
}