package com.deliveranything.domain.user.profile.dto.customer;

import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import lombok.Builder;

/**
 * 고객 프로필 상세 정보 DTO 로그인/프로필 전환 응답에 포함
 */
@Builder
public record CustomerProfileDetail(
    Long profileId,
    String nickname,
    String profileImageUrl,
    Long defaultAddressId,
    String customerPhoneNumber
) {

  /**
   * CustomerProfile 엔티티로부터 DTO 생성
   */
  public static CustomerProfileDetail from(CustomerProfile profile) {
    if (profile == null) {
      return null;
    }

    return CustomerProfileDetail.builder()
        .profileId(profile.getId())
        .nickname(profile.getNickname())
        .profileImageUrl(profile.getProfileImageUrl())
        .defaultAddressId(profile.getDefaultAddressId())
        .customerPhoneNumber(profile.getCustomerPhoneNumber())
        .build();
  }
}