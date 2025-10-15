package com.deliveranything.domain.product.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductSearchRequest(
    @Size(max = 50, message = "검색어는 50자를 초과할 수 없습니다.")
    String searchText,

    String nextPageToken,

    @Min(value = 1, message = "limit 값은 1 이상이어야 합니다.")
    @Max(value = 50, message = "limit 값은 50을 초과할 수 없습니다.")
    Integer limit
) {
  public ProductSearchRequest {
    if (limit == null) {
      limit = 20;
    }
  }
}