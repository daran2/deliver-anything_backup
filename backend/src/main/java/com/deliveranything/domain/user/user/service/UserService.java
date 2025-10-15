package com.deliveranything.domain.user.user.service;

import com.deliveranything.domain.user.user.entity.User;
import com.deliveranything.domain.user.user.repository.UserRepository;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // ========== 기본 사용자 관리 ==========

  public User findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public boolean existsByPhoneNumber(String phoneNumber) {
    return userRepository.existsByPhoneNumber(phoneNumber);
  }

  public long count() {
    return userRepository.count();
  }

  // ========== 사용자 정보 수정 ==========

  /**
   * 사용자 정보 수정 (이름, 전화번호)
   */
  @Transactional
  public User updateUserInfo(Long userId, String username, String phoneNumber) {
    User user = findById(userId);

    // 전화번호 중복 체크 (자신의 전화번호가 아닌 경우만)
    if (phoneNumber != null && !phoneNumber.equals(user.getPhoneNumber())) {
      if (userRepository.existsByPhoneNumber(phoneNumber)) {
        throw new CustomException(ErrorCode.USER_PHONE_ALREADY_EXIST);
      }
    }

    // User 엔티티에 업데이트 메서드 호출
    user.updateUserInfo(username, phoneNumber);
    User updatedUser = userRepository.save(user);

    log.info("사용자 정보 수정 완료: userId={}, name={}", userId, username);
    return updatedUser;
  }

  // ========== 계정 관리 ==========

  /**
   * 비밀번호 변경 (현재 비밀번호 검증 포함)
   */
  @Transactional
  public void changePassword(Long userId, String currentPassword, String newPassword) {
    User user = findById(userId);

    // 현재 비밀번호 검증
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    String encodedNewPassword = passwordEncoder.encode(newPassword);
    user.updatePassword(encodedNewPassword);
    userRepository.save(user);

    log.info("사용자 비밀번호 변경 완료: userId={}", userId);
  }

  /**
   * 비밀번호 업데이트 (관리자용 또는 비밀번호 재설정용)
   */
  @Transactional
  public void updatePassword(Long userId, String newPassword) {
    User user = findById(userId);

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.updatePassword(encodedPassword);
    userRepository.save(user);

    log.info("사용자 비밀번호 업데이트 완료: userId={}", userId);
  }

  /**
   * 관리자 권한 부여 ( 최상위 관리자만 호출 가능) - isAdmin이 있어 우선 추가만 해놓음
   */
  @Transactional
  public void grantAdminRole(Long userId) {
    User user = findById(userId);
    user.grantAdminRole();  // 위에서 추가한 메서드
    userRepository.save(user);
    log.info("관리자 권한 부여 완료: userId={}", userId);
  }

  /**
   * 관리자 권한 제거
   */
  @Transactional
  public void revokeAdminRole(Long userId) {
    User user = findById(userId);
    user.revokeAdminRole();
    userRepository.save(user);
    log.info("관리자 권한 제거 완료: userId={}", userId);
  }

}