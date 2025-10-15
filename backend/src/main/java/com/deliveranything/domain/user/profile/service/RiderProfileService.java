package com.deliveranything.domain.user.profile.service;

import com.deliveranything.domain.user.profile.entity.RiderProfile;
import com.deliveranything.domain.user.profile.enums.RiderToggleStatus;
import com.deliveranything.domain.user.profile.repository.ProfileRepository;
import com.deliveranything.domain.user.profile.repository.RiderProfileRepository;
import com.deliveranything.domain.user.user.repository.UserRepository;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiderProfileService {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final RiderProfileRepository riderProfileRepository;

  // ========== 프로필 조회 ==========

  public RiderProfile getRiderProfileById(Long riderProfileId) {
    return riderProfileRepository.findById(riderProfileId)
        .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));
  }

  public RiderProfile getProfile(Long userId) {
    return riderProfileRepository.findById(userId).orElse(null);
  }

  public boolean hasProfile(Long userId) {
    return getProfile(userId) != null;
  }

  // ========== 프로필 정보 수정 ==========

  @Transactional
  public boolean updateProfileByProfileId(Long profileId, String nickname, String profileImageUrl) {
    RiderProfile profile = getRiderProfileById(profileId);

    profile.updateProfile(nickname, profileImageUrl);
    riderProfileRepository.save(profile);

    log.info("라이더 프로필 수정 완료: profileId={}, nickname={}", profileId, nickname);
    return true;
  }

  @Transactional
  public void updateDeliveryArea(Long riderProfileId, String newArea) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    profile.updateDeliveryArea(newArea);
    riderProfileRepository.save(profile);
    log.info("활동 지역 변경 완료 - Profile ID: {}, New Area: {}", riderProfileId, newArea);
  }

  @Transactional
  public void updateBankInfo(Long riderProfileId, String bankName, String accountNumber,
      String accountHolder) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    profile.updateBankInfo(bankName, accountNumber, accountHolder);
    riderProfileRepository.save(profile);
    log.info("은행 계좌 정보 수정 완료 - Profile ID: {}, Bank: {}", riderProfileId, bankName);
  }

  // ========== 배달 상태 관리 (String 기반) ==========

  /**
   * 배달 상태 토글 (ON ↔ OFF)
   */
  @Transactional
  public void toggleDeliveryStatus(Long riderProfileId) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    profile.toggleStatus();
    riderProfileRepository.save(profile);
    log.info("배달원 배달 상태 토글 완료 - Profile ID: {}, 현재 상태: {}",
        riderProfileId, profile.getToggleStatus());
  }

  /**
   * 배달 상태 직접 설정 (String → Enum 변환) Delivery 도메인 DTO와의 호환을 위해 String 사용
   */
  @Transactional
  public void updateDeliveryStatus(Long riderProfileId, String statusStr) {
    RiderProfile profile = getRiderProfileById(riderProfileId);

    // String을 Enum으로 변환
    RiderToggleStatus status = RiderToggleStatus.fromString(statusStr);
    profile.updateToggleStatus(status);
    riderProfileRepository.save(profile);

    log.info("배달 상태 변경 완료 - Profile ID: {}, 상태: {}", riderProfileId, status);
  }

  /**
   * 배달 시작 (ON)
   */
  @Transactional
  public void startDelivery(Long riderProfileId) {
    updateDeliveryStatus(riderProfileId, "ON");
    log.info("배달 시작 - Profile ID: {}", riderProfileId);
  }

  /**
   * 배달 종료 (OFF)
   */
  @Transactional
  public void stopDelivery(Long riderProfileId) {
    updateDeliveryStatus(riderProfileId, "OFF");
    log.info("배달 종료 - Profile ID: {}", riderProfileId);
  }

  // ========== 조회 ==========

  public boolean isAvailableForDelivery(Long riderProfileId) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    return profile.isAvailableForDelivery();
  }

  public RiderToggleStatus getDeliveryStatus(Long riderProfileId) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    return profile.getToggleStatus();
  }

  public String getDeliveryArea(Long riderProfileId) {
    RiderProfile profile = getRiderProfileById(riderProfileId);
    return profile.getArea();
  }
}