package com.deliveranything.domain.store.category.repository;

import com.deliveranything.domain.store.category.entity.StoreCategory;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

  default StoreCategory getById(Long categoryId) {
    return findById(categoryId)
        .orElseThrow(() -> new CustomException(ErrorCode.STORE_CATEGORY_NOT_FOUND));
  }
}
