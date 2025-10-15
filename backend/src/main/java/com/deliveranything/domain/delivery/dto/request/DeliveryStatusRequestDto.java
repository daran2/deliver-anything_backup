package com.deliveranything.domain.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DeliveryStatusRequestDto(
    @NotNull
    @Schema(description = "라이더 현재 상태")
    @Pattern(regexp = "^(PENDING|ASSIGNED|PICKED_UP|IN_PROGRESS|COMPLETED)$")
    String status
) {

}
