package com.obolonyk.webflux_playground.sec04.dto;

public record CustomerDto(
        Integer id,
        String name,
        String email) {
}
