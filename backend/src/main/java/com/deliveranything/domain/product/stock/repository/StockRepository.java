package com.deliveranything.domain.product.stock.repository;

import com.deliveranything.domain.product.stock.entity.Stock;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

  Optional<Stock> findByProductId(Long productId);

  default Stock getByProductId(Long productId) {
    return findByProductId(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
  }
}
