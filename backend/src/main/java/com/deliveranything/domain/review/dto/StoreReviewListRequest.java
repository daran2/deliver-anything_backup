package com.deliveranything.domain.review.dto;

import com.deliveranything.domain.review.enums.StoreReviewSortType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상점 리뷰 리스트 요청 DTO")
public record StoreReviewListRequest(
    @Schema(description = "정렬 기준")
    StoreReviewSortType sort,
    @Schema(description = "커서")
    String cursor,
    @Schema(description = "페이지 당 사이즈")
    int size
) {

}
