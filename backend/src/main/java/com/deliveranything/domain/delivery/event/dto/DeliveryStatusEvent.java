package com.deliveranything.domain.delivery.event.dto;

import com.deliveranything.domain.delivery.enums.DeliveryStatus;
import lombok.Builder;

@Builder
public record DeliveryStatusEvent(
    Long deliveryId,
    Long orderId,
    Long riderProfileId,
    Long customerProfileId,
    Long sellerProfileId,
    DeliveryStatus status,
    Long occurredAtEpochMs,
    DeliveryStatus nextStatus
) {

}
