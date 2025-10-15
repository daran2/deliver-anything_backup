package com.deliveranything.domain.auth.service;

import com.deliveranything.domain.auth.dto.VerificationSendRequest;
import com.deliveranything.domain.auth.dto.VerificationVerifyRequest;
import com.deliveranything.domain.auth.entity.VerificationToken;
import com.deliveranything.domain.auth.enums.VerificationPurpose;
import com.deliveranything.domain.auth.enums.VerificationType;
import com.deliveranything.domain.auth.repository.VerificationTokenRepository;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationService {

  private final VerificationTokenRepository verificationTokenRepository;
  private final EmailService emailService;
  private final RedisTemplate<String, String> redisTemplate;

  @Value("${custom.email.verification.expirationMinutes}")
  private int verificationExpirationMinutes;

  private static final String REDIS_KEY_PREFIX = "verification:";
  private static final String RATE_LIMIT_PREFIX = "verification_limit:";
  private static final int MAX_ATTEMPTS_PER_HOUR = 5;

  /**
   * 인증 코드 발송
   */
  @Transactional
  public void sendVerificationCode(VerificationSendRequest request) {
    String identifier = request.identifier();
    VerificationType type = request.verificationType();
    VerificationPurpose purpose = request.purpose();

    // Rate Limiting 체크
    checkRateLimit(identifier);

    // 이메일 인증만 지원 (SMS는 추가 구현 필요)
    if (type != VerificationType.EMAIL) {
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    // 기존 인증 코드가 있다면 삭제
    deleteExistingVerification(identifier, type, purpose);

    // 새 인증 토큰 생성
    VerificationToken token = VerificationToken.builder()
        .identifier(identifier)
        .verificationType(type)
        .purpose(purpose)
        .validMinutes(verificationExpirationMinutes)
        .build();

    verificationTokenRepository.save(token);

    // Redis에도 저장 (빠른 조회용)
    String redisKey = buildRedisKey(identifier, type, purpose);
    redisTemplate.opsForValue().set(
        redisKey,
        token.getVerificationCode(),
        Duration.ofMinutes(verificationExpirationMinutes)
    );

    // 이메일 발송
    emailService.sendVerificationEmail(identifier, token.getVerificationCode());

    // Rate Limit 카운트 증가
    incrementRateLimit(identifier);

    log.info("인증 코드 발송 완료: identifier={}, type={}, purpose={}",
        identifier, type, purpose);
  }

  /**
   * 인증 코드 검증
   */
  @Transactional
  public boolean verifyCode(VerificationVerifyRequest request) {
    String identifier = request.identifier();
    String inputCode = request.verificationCode();
    VerificationType type = request.verificationType();
    VerificationPurpose purpose = request.purpose();

    // Redis에서 먼저 확인 (빠른 검증)
    String redisKey = buildRedisKey(identifier, type, purpose);
    String storedCode = redisTemplate.opsForValue().get(redisKey);

    if (storedCode == null) {
      log.warn("인증 코드가 만료되었거나 존재하지 않음: identifier={}", identifier);
      throw new CustomException(ErrorCode.TOKEN_EXPIRED);
    }

    if (!storedCode.equals(inputCode)) {
      log.warn("인증 코드 불일치: identifier={}", identifier);
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    // DB에서도 확인 및 사용 처리
    VerificationToken token = verificationTokenRepository
        .findTopByIdentifierAndVerificationTypeAndPurposeOrderByCreatedAtDesc(
            identifier, type, purpose)
        .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));

    if (!token.verifyCode(inputCode)) {
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    // 사용 처리
    token.markAsUsed();
    verificationTokenRepository.save(token);

    // Redis에서 삭제
    redisTemplate.delete(redisKey);

    log.info("인증 코드 검증 완료: identifier={}", identifier);
    return true;
  }

  /**
   * Rate Limiting 체크 (1시간에 5회 제한)
   */
  private void checkRateLimit(String identifier) {
    String limitKey = RATE_LIMIT_PREFIX + identifier;
    String countStr = redisTemplate.opsForValue().get(limitKey);
    int count = countStr != null ? Integer.parseInt(countStr) : 0;

    if (count >= MAX_ATTEMPTS_PER_HOUR) {
      log.warn("인증 코드 발송 제한 초과: identifier={}", identifier);
      throw new CustomException(ErrorCode.TOKEN_REFRESH_RATE_LIMIT_EXCEEDED);
    }
  }

  /**
   * Rate Limit 카운트 증가
   */
  private void incrementRateLimit(String identifier) {
    String limitKey = RATE_LIMIT_PREFIX + identifier;
    Long count = redisTemplate.opsForValue().increment(limitKey);

    if (count != null && count == 1) {
      redisTemplate.expire(limitKey, Duration.ofHours(1));
    }
  }

  /**
   * 기존 인증 코드 삭제
   */
  private void deleteExistingVerification(
      String identifier,
      VerificationType type,
      VerificationPurpose purpose
  ) {
    String redisKey = buildRedisKey(identifier, type, purpose);
    redisTemplate.delete(redisKey);
  }

  /**
   * Redis 키 생성
   */
  private String buildRedisKey(
      String identifier,
      VerificationType type,
      VerificationPurpose purpose
  ) {
    return REDIS_KEY_PREFIX + type.name() + ":" + purpose.name() + ":" + identifier;
  }
}