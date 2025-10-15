package com.deliveranything.domain.user.profile.dto.rider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 배달원 배달 가능 상태 변경 요청 DTO. Delivery 도메인의 RiderToggleStatusRequestDto와 동일한 검증 규칙 사용
 */
public record RiderStatusUpdateRequest(
    @NotNull(message = "배달 상태는 필수 입력 사항입니다.")
    @Schema(description = "라이더 배달 가능 상태", example = "ON")
    @Pattern(regexp = "^(ON|OFF)$", message = "배달 상태는 'ON' 또는 'OFF' 여야 합니다.")
    String riderStatus
) {

}