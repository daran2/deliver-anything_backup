package com.deliveranything.domain.delivery.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record DeliveredDetailsDto(
    Long orderId,
    String storeName,
    LocalDateTime completedAt,
    String customerAddress,
    String settlementStatus,
    Integer deliveryCharge
) {

}
