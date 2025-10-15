package com.deliveranything.domain.user.profile.dto.customer;

import jakarta.validation.constraints.Size;

/**
 * 배송지 수정 요청 DTO
 */
public record AddressUpdateRequest(
    @Size(max = 100, message = "배송지 이름은 100자 이하로 입력해주세요.")
    String addressName,

    @Size(max = 300, message = "주소는 300자 이하로 입력해주세요.")
    String address,

    Double latitude,

    Double longitude
) {
  // 최소 하나의 필드는 입력되어야 함을 컨트롤러에서 검증
}