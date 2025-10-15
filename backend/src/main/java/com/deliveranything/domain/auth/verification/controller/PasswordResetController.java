package com.deliveranything.domain.auth.verification.controller;

import com.deliveranything.domain.auth.verification.dto.PasswordResetConfirmRequest;
import com.deliveranything.domain.auth.verification.dto.PasswordResetRequest;
import com.deliveranything.domain.auth.verification.dto.PasswordResetVerifyRequest;
import com.deliveranything.domain.auth.verification.service.PasswordResetService;
import com.deliveranything.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "비밀번호 재설정 API", description = "비밀번호 찾기 및 재설정")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @PostMapping("/reset/request")
  @Operation(
      summary = "비밀번호 재설정 요청",
      description = "이메일로 인증 코드를 발송합니다. (6자리 숫자)"
  )
  public ResponseEntity<ApiResponse<Void>> requestPasswordReset(
      @Valid @RequestBody PasswordResetRequest request
  ) {
    log.info("비밀번호 재설정 요청: email={}", request.email());

    passwordResetService.requestPasswordReset(request.email());

    return ResponseEntity.ok(
        ApiResponse.success("인증 코드가 이메일로 발송되었습니다.", null)
    );
  }

  @PostMapping("/reset/verify")
  @Operation(
      summary = "인증 코드 검증 및 재설정 토큰 발급",
      description = "인증 코드를 검증하고 비밀번호 재설정용 토큰을 발급받습니다."
  )
  public ResponseEntity<ApiResponse<Map<String, String>>> verifyCode(
      @Valid @RequestBody PasswordResetVerifyRequest request
  ) {
    log.info("인증 코드 검증 요청: email={}", request.email());

    String resetToken = passwordResetService.verifyCodeAndIssueResetToken(
        request.email(),
        request.verificationCode()
    );

    return ResponseEntity.ok(
        ApiResponse.success(
            "인증이 완료되었습니다. 새 비밀번호를 설정해주세요.",
            Map.of("resetToken", resetToken)
        )
    );
  }

  @PostMapping("/reset/confirm")
  @Operation(
      summary = "새 비밀번호 설정",
      description = "재설정 토큰을 사용하여 새로운 비밀번호를 설정합니다."
  )
  public ResponseEntity<ApiResponse<Void>> confirmPasswordReset(
      @Valid @RequestBody PasswordResetConfirmRequest request
  ) {
    log.info("비밀번호 재설정 확정 요청");

    passwordResetService.resetPassword(
        request.resetToken(),
        request.newPassword()
    );

    return ResponseEntity.ok(
        ApiResponse.success("비밀번호가 성공적으로 변경되었습니다.", null)
    );
  }
}