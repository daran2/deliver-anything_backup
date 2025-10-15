package com.deliveranything.domain.product.product.event;

public record ProductKeywordsChangedEvent(
    Long storeId,
    Long productId
) {

}