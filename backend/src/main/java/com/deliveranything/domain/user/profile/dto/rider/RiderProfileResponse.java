package com.deliveranything.domain.user.profile.dto.rider;

import com.deliveranything.domain.user.profile.entity.RiderProfile;
import com.deliveranything.domain.user.profile.enums.RiderToggleStatus;
import lombok.Builder;

/**
 * 배달원 프로필 조회 응답 DTO
 */
@Builder
public record RiderProfileResponse(
    Long profileId,
    String nickname,
    String profileImageUrl,
    RiderToggleStatus toggleStatus,
    String area,
    String licenseNumber,
    String bankName,
    String bankAccountNumber,
    String bankAccountHolderName,
    String riderPhoneNumber,
    boolean isAvailableForDelivery
) {

  public static RiderProfileResponse from(RiderProfile profile) {
    if (profile == null) {
      return null;
    }

    return RiderProfileResponse.builder()
        .profileId(profile.getId())
        .nickname(profile.getNickname())
        .profileImageUrl(profile.getProfileImageUrl())
        .toggleStatus(profile.getToggleStatus())
        .area(profile.getArea())
        .licenseNumber(profile.getLicenseNumber())
        .bankName(profile.getBankName())
        .bankAccountNumber(profile.getBankAccountNumber())
        .bankAccountHolderName(profile.getBankAccountHolderName())
        .riderPhoneNumber(profile.getRiderPhoneNumber())
        .isAvailableForDelivery(profile.isAvailableForDelivery())
        .build();
  }
}