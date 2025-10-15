package com.deliveranything.domain.product.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    @Size(min = 1, max = 50, message = "상품명은 공백일 수 없으며, 50자를 초과할 수 없습니다.")
    String name,

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    String description,

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "가격은 " + Integer.MAX_VALUE + "원을 초과할 수 없습니다.")
    Integer price,

    @NotBlank(message = "이미지 URL은 필수 입력 값입니다.")
    @Size(min = 1, max = 256, message = "이미지 URL은 공백일 수 없으며, 256자를 초과할 수 없습니다.")
    String imageUrl,

    @NotNull(message = "재고 수량은 필수 입력 값입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    Integer initialStock
) {
  public ProductCreateRequest {
    if (description == null) { description = ""; }
  }
}