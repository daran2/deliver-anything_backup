package com.deliveranything.domain.delivery.dto;

import lombok.Builder;

@Builder
public record RiderLocationDto(
    double latitude,
    double longitude,
    long timestamp  // 밀리초 단위,
    // 변환: Instant.ofEpochMilli(timestamp) → LocalDateTime
) {

}
