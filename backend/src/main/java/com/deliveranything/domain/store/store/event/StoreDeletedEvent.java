package com.deliveranything.domain.store.store.event;

public record StoreDeletedEvent(
    Long storeId,
    StoreEventType type
) {
  public StoreDeletedEvent(Long storeId) {
    this(storeId, StoreEventType.DELETED);
  }
}