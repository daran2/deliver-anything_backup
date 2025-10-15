package com.deliveranything.domain.delivery.dto.response;

import lombok.Builder;

@Builder
public record DeliveringCustomerDetailsDto(
    String customerNickname,
    String customerAddress,
    String customerPhoneNumber,
    String riderNote
) {

}
