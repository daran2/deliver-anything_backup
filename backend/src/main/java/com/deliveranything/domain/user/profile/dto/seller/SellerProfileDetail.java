package com.deliveranything.domain.user.profile.dto.seller;

import com.deliveranything.domain.user.profile.entity.SellerProfile;
import lombok.Builder;

/**
 * 판매자 프로필 상세 정보 DTO 로그인/프로필 전환 응답에 포함
 */
@Builder
public record SellerProfileDetail(
    Long profileId,
    String nickname,
    String profileImageUrl,
    String businessName,
    String businessCertificateNumber,
    String businessPhoneNumber,
    String bankName,
    String accountNumber,
    String accountHolder
) {

  /**
   * SellerProfile 엔티티로부터 DTO 생성
   */
  public static SellerProfileDetail from(SellerProfile profile) {
    if (profile == null) {
      return null;
    }

    return SellerProfileDetail.builder()
        .profileId(profile.getId())
        .nickname(profile.getNickname())
        .profileImageUrl(profile.getProfileImageUrl())
        .businessName(profile.getBusinessName())
        .businessCertificateNumber(profile.getBusinessCertificateNumber())
        .businessPhoneNumber(profile.getBusinessPhoneNumber())
        .bankName(profile.getBankName())
        .accountNumber(profile.getAccountNumber())
        .accountHolder(profile.getAccountHolder())
        .build();
  }
}