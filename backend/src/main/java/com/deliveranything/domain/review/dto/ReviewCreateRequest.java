package com.deliveranything.domain.review.dto;

import com.deliveranything.domain.review.enums.ReviewTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "리뷰 생성 요청 DTO")
public record ReviewCreateRequest(
    @Schema(description = "별점", example = "5")
    @NotNull
    @Min(1)
    @Max(5)
    Integer rating,
    @Schema(description = "코멘트", example = "배송이 빨랐습니다.")
    @NotNull
    @Size(min = 1, max = 300)
    String comment,
    @Schema(description = "리뷰 사진 목록", example = "[\"https://example.com/photo1.jpg\", \"https://example.com/photo2.jpg\"]", required = false)
    @Size(max = 4)
    String[] photoUrls,
    @Schema(description = "리뷰 대상 분류", example = "RIDER", allowableValues = {"RIDER","STORE"})
    @NotNull
    ReviewTargetType targetType,
    @NotNull
    @Schema(description = "리뷰 대상 프로필 id", example = "5")
    Long targetId
) {

}
