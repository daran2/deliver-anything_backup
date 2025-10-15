package com.deliveranything.domain.review.dto;

import com.deliveranything.domain.review.entity.Review;
import com.deliveranything.domain.review.enums.ReviewTargetType;
import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "리뷰 생성 응답 DTO")
public record ReviewCreateResponse(
    @Schema(description = "리뷰 id", example = "1")
    Long id,
    @Schema(description = "별점", example = "5")
    Integer rating,
    @Schema(description = "코멘트", example = "배송이 빨랐습니다.")
    String comment,
    @Schema(description = "리뷰 사진 목록", example = "[\"https://example.com/photo1.jpg\"]")
    List<String> photoUrls,
    @Schema(description = "작성자 프로필 정보")
    CustomerProfile customerProfile,
    @Schema(description = "리뷰 대상 분류", example = "RIDER", allowableValues = {"RIDER", "STORE"})
    ReviewTargetType targetType,
    @Schema(description = "리뷰 대상 id (상점 id or 배달원프로필 id)", example = "5")
    Long targetId
) {

  public static ReviewCreateResponse from(Review review, List<String> reviewPhotoUrls,
      CustomerProfile customerProfile) {
    return new ReviewCreateResponse(
        review.getId(),
        review.getRating(),
        review.getComment(),
        reviewPhotoUrls,
        customerProfile,
        review.getTargetType(),
        review.getTargetId()
    );
  }

}
