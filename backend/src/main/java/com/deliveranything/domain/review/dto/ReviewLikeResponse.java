package com.deliveranything.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 좋아요 응답 DTO")
public record ReviewLikeResponse(
    @Schema(description = "리뷰 id", example = "1")
    Long reviewId,
    @Schema(description = "리뷰 좋아요 개수", example = "32")
    Long likeCount,
    @Schema(description = "유저의 좋아요 여부", example = "true")
    Boolean likedByMe
) {}
