package com.deliveranything.domain.delivery.dto.response;

import lombok.Builder;

@Builder
public record CurrentDeliveringDetailsDto(
    Long orderId,
    DeliveringStoreDetailsDto storeDetails,
    DeliveringCustomerDetailsDto customerDetails,
    double remainingTime,
    double expectedTime
) {

}
