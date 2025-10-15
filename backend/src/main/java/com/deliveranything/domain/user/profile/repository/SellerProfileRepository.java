package com.deliveranything.domain.user.profile.repository;

import com.deliveranything.domain.user.profile.entity.SellerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {

  // 사업자등록번호로 중복 체크
  boolean existsByBusinessCertificateNumber(String businessCertificateNumber);

  // 사업자등록번호로 조회
  Optional<SellerProfile> findByBusinessCertificateNumber(String businessCertificateNumber);

  // 사용자 ID로 판매자 프로필 조회
  @Query("SELECT sp FROM SellerProfile sp WHERE sp.profile.user.id = :userId AND sp.profile.isActive = true")
  Optional<SellerProfile> findByUserId(@Param("userId") Long userId);

  // Profile ID로 조회
  @Query("SELECT sp FROM SellerProfile sp WHERE sp.profile.id = :profileId")
  Optional<SellerProfile> findByProfileId(@Param("profileId") Long profileId);
}