package com.obolonyk.webflux_playground.sec03.dto;

public record CustomerDto(
        Integer id,
        String name,
        String email) {
}
