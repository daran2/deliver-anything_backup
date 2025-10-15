package com.deliveranything.domain.review.dto;

import com.deliveranything.domain.review.entity.Review;
import com.deliveranything.domain.review.enums.ReviewTargetType;
import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "리뷰 디폴트 응답 DTO")
public record ReviewResponse(
    @Schema(description = "리뷰 id", example = "1")
    Long id,
    @Schema(description = "별점", example = "5")
    Integer rating,
    @Schema(description = "코멘트", example = "배송이 빨랐습니다.")
    String comment,
    @Schema(description = "리뷰 사진 목록", example = "[\"https://example.com/photo1.jpg\"]")
    List<String> photoUrls,
    @Schema(description = "리뷰 대상 분류", example = "RIDER", allowableValues = {"RIDER", "STORE"})
    ReviewTargetType targetType,
    @Schema(description = "리뷰 대상 id (상점 id or 배달원프로필 id)", example = "5")
    Long targetId,
    @Schema(description = "리뷰 생성 시각", example = "2025-09-29T08:00:00")
    LocalDateTime createdAt,
    @Schema(description = "리뷰 마지막 수정 시각", example = "2025-09-29T08:30:00")
    LocalDateTime updatedAt,
    @Schema(description = "리뷰 좋아요 개수", example = "32")
    Long likeCount,
    @Schema(description = "작성자 프로필 정보")
    CustomerProfile customerProfile
) {

  public static ReviewResponse from(Review review, List<String> photoUrls, Long likeCount) {
    return new ReviewResponse(
        review.getId(),
        review.getRating(),
        review.getComment(),
        photoUrls,
        review.getTargetType(),
        review.getTargetId(),
        review.getCreatedAt(),
        review.getUpdatedAt(),
        likeCount,
        review.getCustomerProfile()
    );
  }
}
