package com.deliveranything.domain.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RiderDecisionRequestDto(
    @NotNull
    String orderId,

    @NotNull
    @Schema(description = "라이더 수락/거절")
    @Pattern(regexp = "^(RIDER_ASSIGNED|REJECTED)$")
    String decisionStatus,

    @NotNull
    @Schema(description = "라이더 예상 도착 시간 (분)")
    Double etaMinutes
) {

}