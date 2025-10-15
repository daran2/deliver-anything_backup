package com.deliveranything.domain.delivery.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TodayDeliveringResponseDto(
    LocalDateTime now,
    String currentStatus,
    Long todayDeliveryCount,
    Long todayEarningAmount,
    Double avgDeliveryTime
) {

}
