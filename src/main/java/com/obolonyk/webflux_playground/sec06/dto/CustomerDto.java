package com.obolonyk.webflux_playground.sec06.dto;

public record CustomerDto(
        Integer id,
        String name,
        String email) {
}
