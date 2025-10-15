package com.deliveranything.domain.user.profile.entity;

import com.deliveranything.global.entity.BaseEntity;
import com.deliveranything.global.util.PointUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "customer_addresses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerAddress extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_profile_id", nullable = false)
  private CustomerProfile customerProfile; // 이제 Profile의 전역 고유 ID를 참조

  @Column(name = "address_name", nullable = false, columnDefinition = "VARCHAR(100)")
  private String addressName;

  @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(300)")
  private String address;

  @Column(columnDefinition = "geometry", nullable = false)
  private Point location;

  @Builder
  public CustomerAddress(CustomerProfile customerProfile, String addressName, String address,
      Double latitude, Double longitude) {
    this.customerProfile = customerProfile;
    this.addressName = addressName;
    this.address = address;
    this.location = PointUtil.createPoint(latitude, longitude);
  }

  // 비즈니스 메서드
  public void updateAddress(String addressName, String address, Double latitude, Double longitude) {
    if (addressName != null && !addressName.isBlank()) {
      this.addressName = addressName;
    }
    if (address != null && !address.isBlank()) {
      this.address = address;
    }
    if (latitude != null && longitude != null) {
      this.location = PointUtil.createPoint(latitude, longitude);
    }
  }

  /**
   * 기본 주소인지 확인 (Profile ID 기반)
   */
  public boolean isDefault() {
    if (customerProfile == null || customerProfile.getDefaultAddressId() == null) {
      return false;
    }
    return customerProfile.getDefaultAddressId().equals(this.getId());
  }

  /**
   * 특정 프로필의 주소인지 확인 (Profile ID 기반)
   */
  public boolean belongsToProfile(Long profileId) {
    return customerProfile != null && customerProfile.getId().equals(profileId);
  }

  /**
   * 사용자 ID 조회 헬퍼
   */
  public Long getUserId() {
    return customerProfile != null ? customerProfile.getUserId() : null;
  }

  /**
   * Profile ID 조회 헬퍼 (전역 고유 ID)
   */
  public Long getProfileId() {
    return customerProfile != null ? customerProfile.getId() : null;
  }
}// 위도/경도를 받아서 Point 객체로 변환
//    this.location = PointUtil.createPoint(latitude, longitude);