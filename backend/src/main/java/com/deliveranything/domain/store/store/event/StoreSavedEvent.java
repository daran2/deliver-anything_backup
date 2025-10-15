package com.deliveranything.domain.store.store.event;

public record StoreSavedEvent(
    Long storeId,
    StoreEventType type
) {
  public StoreSavedEvent(Long storeId) {
    this(storeId, StoreEventType.SAVED);
  }
}
