package com.deliveranything.domain.delivery.event.dto;

import com.deliveranything.domain.delivery.dto.OrderDetailsDto;
import com.deliveranything.domain.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record RiderNotificationDto(
    OrderDetailsDto orderDetailsDto,
    String riderId,
    Double etaMinutes, // riderId → ETA (분)
    OrderStatus orderDeliveryStatus
) {

}
