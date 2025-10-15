package com.deliveranything.domain.user.profile.repository;

import com.deliveranything.domain.user.profile.entity.CustomerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

  // Profile ID로 조회 (이제 Primary Key)
  Optional<CustomerProfile> findById(Long profileId);

  // 사용자 ID로 고객 프로필 조회
  @Query("SELECT cp FROM CustomerProfile cp WHERE cp.profile.user.id = :userId AND cp.profile.isActive = true")
  Optional<CustomerProfile> findByUserId(@Param("userId") Long userId);

  // Profile 엔티티로 조회
  @Query("SELECT cp FROM CustomerProfile cp WHERE cp.profile.id = :profileId")
  Optional<CustomerProfile> findByProfileId(@Param("profileId") Long profileId);
}