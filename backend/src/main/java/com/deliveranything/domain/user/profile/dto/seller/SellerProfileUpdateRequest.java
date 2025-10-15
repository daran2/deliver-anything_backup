package com.deliveranything.domain.user.profile.dto.seller;

import jakarta.validation.constraints.Size;

/**
 * 판매자 프로필 수정 요청 DTO (닉네임, 프로필 이미지)
 */
public record SellerProfileUpdateRequest(
    @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하로 입력해주세요.")
    String nickname,

    @Size(max = 500, message = "프로필 이미지 URL은 500자 이하로 입력해주세요.")
    String profileImageUrl
) {
  // 최소 하나의 필드는 입력되어야 함을 컨트롤러에서 검증
}