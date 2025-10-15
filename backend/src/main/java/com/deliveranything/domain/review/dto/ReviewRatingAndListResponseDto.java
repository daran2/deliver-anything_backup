package com.deliveranything.domain.review.dto;

import com.deliveranything.global.common.CursorPageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 평균 평점 포함 리스트 응답 DTO")
public record ReviewRatingAndListResponseDto(
    @Schema(description = "리뷰 평균 평점", example = "3.5")
    Double avgRating,
    @Schema(description = "리뷰 리스트")
    CursorPageResponse<ReviewResponse> reviews
) {

}
