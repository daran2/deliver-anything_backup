package com.deliveranything.domain.user.profile.repository;

import com.deliveranything.domain.user.profile.entity.Profile;
import com.deliveranything.domain.user.profile.enums.ProfileType;
import com.deliveranything.domain.user.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

  // 사용자의 특정 타입 프로필 조회
  Optional<Profile> findByUserAndType(User user, ProfileType type);

  // 사용자의 활성 프로필 목록 조회
  @Query("SELECT p FROM Profile p WHERE p.user = :user AND p.isActive = true")
  List<Profile> findActiveProfilesByUser(@Param("user") User user);

  // 사용자의 모든 프로필 조회
  List<Profile> findAllByUser(User user);

  // 사용자ID와 프로필 타입으로 조회
  @Query("SELECT p FROM Profile p WHERE p.user.id = :userId AND p.type = :type AND p.isActive = true")
  Optional<Profile> findByUserIdAndType(@Param("userId") Long userId,
      @Param("type") ProfileType type);

  // 활성 상태인 특정 타입의 모든 프로필 조회
  List<Profile> findByTypeAndIsActiveTrue(ProfileType type);

  // 사용자ID와 프로필 타입으로 존재 여부 확인
  boolean existsByUserIdAndType(Long userId, ProfileType profileType);
}