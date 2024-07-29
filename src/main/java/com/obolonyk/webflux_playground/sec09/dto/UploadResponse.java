package com.obolonyk.webflux_playground.sec09.dto;

import java.util.UUID;

public record UploadResponse(UUID id, Long productCount) {
}
