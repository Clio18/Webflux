package com.obolonyk.webflux_playground.sec05.dto;

public record CustomerDto(
        Integer id,
        String name,
        String email) {
}
