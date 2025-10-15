package com.deliveranything.domain.user.profile.dto.rider;

import com.deliveranything.domain.user.profile.entity.RiderProfile;
import lombok.Builder;

/**
 * 배달원 프로필 상세 정보 DTO 로그인/프로필 전환 응답에 포함
 */
@Builder
public record RiderProfileDetail(
    Long profileId,
    String nickname,
    String profileImageUrl,
    String toggleStatus,
    String area,
    String licenseNumber,
    String riderPhoneNumber
) {

  /**
   * RiderProfile 엔티티로부터 DTO 생성
   */
  public static RiderProfileDetail from(RiderProfile profile) {
    if (profile == null) {
      return null;
    }

    return RiderProfileDetail.builder()
        .profileId(profile.getId())
        .nickname(profile.getNickname())
        .profileImageUrl(profile.getProfileImageUrl())
        .toggleStatus(profile.getToggleStatus().name())
        .area(profile.getArea())
        .licenseNumber(profile.getLicenseNumber())
        .riderPhoneNumber(profile.getRiderPhoneNumber())
        .build();
  }
}