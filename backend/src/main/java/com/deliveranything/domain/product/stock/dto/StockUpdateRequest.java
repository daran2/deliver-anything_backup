package com.deliveranything.domain.product.stock.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(
    @NotNull(message = "재고 변경 값은 필수입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상 2,147,483,647 이하만 가능합니다.")
    @Max(value = Integer.MAX_VALUE, message = "재고 수량은 0 이상 2,147,483,647 이하만 가능합니다.")
    Integer stockChange
) {

}
