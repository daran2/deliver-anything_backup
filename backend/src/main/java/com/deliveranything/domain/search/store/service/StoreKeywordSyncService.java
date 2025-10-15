package com.deliveranything.domain.search.store.service;

import com.deliveranything.domain.product.product.repository.ProductRepository;
import com.deliveranything.domain.search.store.repository.StoreSearchRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreKeywordSyncService {

  private final ProductRepository productRepository;
  private final StoreSearchRepository storeSearchRepository;

  @Transactional(readOnly = true)
  public void syncKeywords(Long storeId) {

    storeSearchRepository.findById(storeId).ifPresent(storeDocument -> {

      Set<String> allKeywords = productRepository.findAllByStoreId(storeId).stream()
          .flatMap(p -> p.getKeywords().stream())
          .collect(Collectors.toSet());

      storeDocument.setKeywords(allKeywords.stream().toList());
      storeSearchRepository.save(storeDocument);
    });
  }
}
