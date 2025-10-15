package com.deliveranything.domain.search.store.repository;

import com.deliveranything.domain.search.store.document.StoreDocument;
import com.deliveranything.domain.search.store.dto.StoreSearchRequest;
import com.deliveranything.global.common.CursorPageResponse;

public interface StoreSearchRepositoryCustom {
  CursorPageResponse<StoreDocument> search(StoreSearchRequest request);
}
