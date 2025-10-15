package com.deliveranything.domain.product.product.repository;

import com.deliveranything.domain.product.product.dto.ProductSearchRequest;
import com.deliveranything.domain.product.product.entity.Product;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
  Slice<Product> search(Long storeId, ProductSearchRequest request);
}