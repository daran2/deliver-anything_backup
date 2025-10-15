package com.deliveranything.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 좋아요 이벤트 요청 DTO")
public record ReviewLikeEvent(
    @Schema(description = "리뷰 id", example = "1")
    Long reviewId,
    @Schema(description = "리뷰 좋아요 개수", example = "32")
    int likeCount
) {

}
