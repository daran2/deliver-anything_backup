package com.deliveranything.domain.user.profile.repository;

import com.deliveranything.domain.user.profile.entity.CustomerAddress;
import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

  // 고객 프로필 ID로 주소 목록 조회 (Profile ID 사용)
  @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerProfile.id = :profileId ORDER BY ca.id ASC")
  List<CustomerAddress> findAddressesByProfileId(@Param("profileId") Long profileId);

  // 기존 메서드 유지 (호환성)
  @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerProfile = :profile ORDER BY ca.id ASC")
  List<CustomerAddress> findAddressesByProfile(@Param("profile") CustomerProfile profile);

  // 고객 프로필별 주소 개수 조회
  long countByCustomerProfile(CustomerProfile profile);

  // Profile ID로 주소 개수 조회
  @Query("SELECT COUNT(ca) FROM CustomerAddress ca WHERE ca.customerProfile.id = :profileId")
  long countByCustomerProfileId(@Param("profileId") Long profileId);
}
