package com.deliveranything.domain.product.product.repository;

import com.deliveranything.domain.product.product.entity.Product;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

  List<Product> findAllByStoreId(Long storeId);

  default Product getById(Long productId) {
    return findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
  }
}
