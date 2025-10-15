package com.deliveranything.domain.product.product.repository;

import com.deliveranything.domain.product.product.dto.ProductSearchRequest;
import com.deliveranything.domain.product.product.entity.Product;
import com.deliveranything.domain.product.product.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QProduct product = QProduct.product;

  @Override
  public Slice<Product> search(Long storeId, ProductSearchRequest request) {
    Pageable pageable = PageRequest.of(0, request.limit());
    int querySize = pageable.getPageSize() + 1;

    List<Product> products = queryFactory
        .selectFrom(product)
        .where(
            product.store.id.eq(storeId),
            containsSearchText(request.searchText()),
            cursorId(request.nextPageToken())
        )
        .limit(querySize)
        .orderBy(product.id.desc())
        .fetch();

    boolean hasNext = products.size() > pageable.getPageSize();
    List<Product> content = hasNext ? products.subList(0, pageable.getPageSize()) : products;

    return new SliceImpl<>(content, pageable, hasNext);
  }

  private BooleanExpression containsSearchText(String searchText) {
    if (!StringUtils.hasText(searchText)) {
      return null;
    }
    return product.name.containsIgnoreCase(searchText)
        .or(product.description.containsIgnoreCase(searchText));
  }

  private BooleanExpression cursorId(String nextPageToken) {
    if (nextPageToken == null) {
      return null;
    }
    try {
      long tokenId = Long.parseLong(nextPageToken);
      return product.id.lt(tokenId);
    } catch (NumberFormatException e) {
      System.err.println("Invalid next page token: " + nextPageToken);
      return null;
    }
  }
}