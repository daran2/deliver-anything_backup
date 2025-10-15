package com.deliveranything.domain.store.store.dto;

import com.deliveranything.domain.store.store.entity.Store;
import com.deliveranything.domain.store.store.enums.StoreStatus;

public record StoreResponse(
    Long id,
    String name,
    String description,
    String roadAddr,
    StoreStatus status,
    String imageUrl,
    String category
) {
  public static StoreResponse from(Store store) {
    return new StoreResponse(
        store.getId(),
        store.getName(),
        store.getDescription(),
        store.getRoadAddr(),
        store.getStatus(),
        store.getImageUrl(),
        store.getStoreCategory().getName()
    );
  }
}
