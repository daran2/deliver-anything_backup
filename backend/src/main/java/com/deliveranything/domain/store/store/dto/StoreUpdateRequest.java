package com.deliveranything.domain.store.store.dto;

import jakarta.validation.constraints.Size;

public record StoreUpdateRequest(
    Long storeCategoryId,
    String name,
    String description,
    String roadAddr,
    Double lat,
    Double lng,
    @Size(min = 1, max = 256, message = "이미지 URL은 공백일 수 없으며, 256자를 초과할 수 없습니다.")
    String imageUrl
) {

}