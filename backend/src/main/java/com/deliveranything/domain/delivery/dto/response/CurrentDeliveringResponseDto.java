package com.deliveranything.domain.delivery.dto.response;

import lombok.Builder;

@Builder
public record CurrentDeliveringResponseDto(
    Long orderId,
    Long deliveryId,
    String storeName,
    String customerAddress,
    Double remainingTime
) {

}
