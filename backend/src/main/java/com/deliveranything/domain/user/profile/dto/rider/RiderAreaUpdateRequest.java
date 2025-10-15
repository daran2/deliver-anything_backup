package com.deliveranything.domain.user.profile.dto.rider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 활동 지역 수정 요청 DTO.  Delivery 도메인의 DeliveryAreaRequestDto와 동일한 구조
 */
public record RiderAreaUpdateRequest(
    @NotNull(message = "활동 지역은 필수 입력 사항입니다.")
    @Schema(description = "배달 가능 지역", example = "서울 강남구")
    String deliveryArea
) {

}