package com.deliveranything.domain.delivery.event.dto;

import com.deliveranything.domain.delivery.enums.DeliveryStatus;

public record OrderStatusUpdateEvent(
    String orderId,
    Long riderId,
    DeliveryStatus status,
    Double eta
) {

}