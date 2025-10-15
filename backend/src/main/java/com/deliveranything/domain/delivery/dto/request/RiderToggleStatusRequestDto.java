package com.deliveranything.domain.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RiderToggleStatusRequestDto(
    @NotNull
    @Schema(description = "라이더 배달 가능 상태", example = "ON 또는 OFF")
    @Pattern(regexp = "^(ON|OFF)$", message = "라이더 상태는 반드시 'ON' 또는 'OFF' 여야 합니다.")
    String riderStatus
) {

}
