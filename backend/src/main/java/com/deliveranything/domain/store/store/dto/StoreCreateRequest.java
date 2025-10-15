package com.deliveranything.domain.store.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StoreCreateRequest(
    @NotNull Long storeCategoryId,
    @NotBlank String name,
    String description,
    @NotBlank String roadAddr,
    @NotNull Double lat,
    @NotNull Double lng,
    @NotBlank(message = "이미지 URL은 필수 입력 값입니다.")
    @Size(min = 1, max = 256, message = "이미지 URL은 공백일 수 없으며, 256자를 초과할 수 없습니다.")
    String imageUrl
) {

}
