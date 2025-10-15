package com.deliveranything.domain.search.store.service;

import com.deliveranything.domain.search.store.document.StoreDocument;
import com.deliveranything.domain.search.store.repository.StoreSearchRepository;
import com.deliveranything.domain.store.store.entity.Store;
import com.deliveranything.domain.store.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSyncService {

  private final StoreService storeService;
  private final StoreSearchRepository storeSearchRepository;

  @Transactional(readOnly = true)
  public void handleSaved(Long storeId) {
    Store store = storeService.getStoreById(storeId);
    StoreDocument doc = StoreDocument.from(store);
    storeSearchRepository.save(doc);
    log.info("Store (ID: {}) synchronized to ES.", storeId);
  }

  public void handleDeleted(Long storeId) {
    storeSearchRepository.deleteById(storeId);
    log.info("Store (ID: {}) deleted from ES.", storeId);
  }
}
