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
@Table(name = "customer_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerProfile extends BaseProfile {

  @Id
  private Long id; // Profile.id와 동일한 값

  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Profile profile;

  @Column(name = "default_address_id")
  private Long defaultAddressId;

  @Column(name = "customer_phone_number", columnDefinition = "VARCHAR(20)")
  private String customerPhoneNumber;

  @Builder
  public CustomerProfile(Profile profile, String nickname, String profileImageUrl
      , String customerPhoneNumber) {
    super(nickname, profileImageUrl);
    this.profile = profile;
    this.customerPhoneNumber = customerPhoneNumber;
  }

  // 비즈니스 메서드
  public void updateProfile(String nickname, String profileImageUrl) {
    if (nickname != null && !nickname.isBlank()) {
      updateNickname(nickname);
    }
    updateProfileImageUrl(profileImageUrl);
  }

  public void updateDefaultAddressId(Long addressId) {
    this.defaultAddressId = addressId;
  }

  // User 정보 접근용 헬퍼 메서드
  public Long getUserId() {
    return profile != null ? profile.getUser().getId() : null;
  }
}