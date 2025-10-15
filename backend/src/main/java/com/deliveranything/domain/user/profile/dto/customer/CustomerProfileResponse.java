package com.deliveranything.domain.user.profile.dto.customer;

import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import lombok.Builder;

/**
 * 고객 프로필 조회 응답 DTO
 */
@Builder
public record CustomerProfileResponse(
    Long profileId,
    String nickname,
    String profileImageUrl,
    Long defaultAddressId,
    String customerPhoneNumber
) {

  /**
   * CustomerProfile 엔티티로부터 DTO 생성
   */
  public static CustomerProfileResponse from(CustomerProfile profile) {
    if (profile == null) {
      return null;
    }

    return CustomerProfileResponse.builder()
        .profileId(profile.getId())
        .nickname(profile.getNickname())
        .profileImageUrl(profile.getProfileImageUrl())
        .defaultAddressId(profile.getDefaultAddressId())
        .customerPhoneNumber(profile.getCustomerPhoneNumber())
        .build();
  }
}