package com.deliveranything.domain.delivery.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeliveryAreaRequestDto(
    @NotNull
    @Schema(description = "배달 가능 지역", example = "서울 강남구")
    String deliveryArea
) {

}
