package com.deliveranything.domain.search.store.dto;

import com.deliveranything.domain.store.store.enums.StoreStatus;

public record StoreSearchResponse(
    Long id,
    String name,
    String roadAddr,
    StoreStatus status,
    String imageUrl,
    String category,
    Double distance,
    int deliveryFee
) {

}