package com.deliveranything.domain.delivery.dto.response;

import lombok.Builder;

@Builder
public record DeliveringStoreDetailsDto(
    String storeName,
    String storeRoadAddress,
    String sellerBusinessPhoneNumber
) {

}
