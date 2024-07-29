package com.obolonyk.webflux_playground.sec08.dto;

import java.util.UUID;

public record UploadResponse(UUID id, Long productCount) {
}
