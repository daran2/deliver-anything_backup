package com.deliveranything.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 수정 요청 DTO")
public record ReviewUpdateRequest(
    @Schema(description = "별점", example = "5")
    Integer rating,
    @Schema(description = "코멘트", example = "배송이 빨랐습니다.")
    String comment,
    @Schema(description = "리뷰 사진 목록", example = "[\"https://example.com/photo1.jpg\"]")
    String[] photoUrls
) {

}
