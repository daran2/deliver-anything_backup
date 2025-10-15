package com.deliveranything.domain.search.store.repository;

import com.deliveranything.domain.search.store.document.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StoreSearchRepository extends ElasticsearchRepository<StoreDocument, Long>, StoreSearchRepositoryCustom {

}