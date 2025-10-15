package com.deliveranything.domain.user.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seller_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerProfile extends BaseProfile {

  @Id
  private Long id; // Profile.id와 동일한 값

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Profile profile;

  @Column(name = "business_name", nullable = false, columnDefinition = "VARCHAR(100)")
  private String businessName;

  @Column(name = "business_certificate_number", nullable = false, unique = true, columnDefinition = "VARCHAR(20)")
  private String businessCertificateNumber;

  @Column(name = "business_phone_number", nullable = false, columnDefinition = "VARCHAR(20)")
  private String businessPhoneNumber;

  @Column(name = "bank_name", nullable = false, columnDefinition = "VARCHAR(50)")
  private String bankName;

  @Column(name = "account_number", nullable = false, columnDefinition = "VARCHAR(50)")
  private String accountNumber;

  @Column(name = "account_holder", nullable = false, columnDefinition = "VARCHAR(50)")
  private String accountHolder;

  @Builder
  public SellerProfile(Profile profile, String nickname, String profileImageUrl,
      String businessName, String businessCertificateNumber, String businessPhoneNumber,
      String bankName, String accountNumber, String accountHolder) {

    super(nickname, profileImageUrl);
    this.profile = profile;
    this.businessName = businessName;
    this.businessCertificateNumber = businessCertificateNumber;
    this.businessPhoneNumber = businessPhoneNumber;
    this.bankName = bankName;
    this.accountNumber = accountNumber;
    this.accountHolder = accountHolder;
  }

  // 비즈니스 메서드
  public void updateProfile(String nickname, String profileImageUrl) {
    if (nickname != null && !nickname.isBlank()) {
      super.updateNickname(nickname); // BaseProfile의 메서드 호출
    }
    if (profileImageUrl != null) {
      super.updateProfileImageUrl(profileImageUrl); // BaseProfile의 메서드 호출
    }
  }

  public void updateBusinessInfo(String businessName, String businessPhoneNumber) {
    if (businessName != null && !businessName.isBlank()) {
      this.businessName = businessName;
    }
    if (businessPhoneNumber != null && !businessPhoneNumber.isBlank()) {
      this.businessPhoneNumber = businessPhoneNumber;
    }
  }

  public void updateBankInfo(String bankName, String accountNumber, String accountHolder) {
    if (bankName != null && !bankName.isBlank()) {
      this.bankName = bankName;
    }
    if (accountNumber != null && !accountNumber.isBlank()) {
      this.accountNumber = accountNumber;
    }
    if (accountHolder != null && !accountHolder.isBlank()) {
      this.accountHolder = accountHolder;
    }
  }

  // User 정보 접근용 헬퍼 메서드
  public Long getUserId() {
    return profile != null
        ? profile.getUser().getId() : null;
  }
}
