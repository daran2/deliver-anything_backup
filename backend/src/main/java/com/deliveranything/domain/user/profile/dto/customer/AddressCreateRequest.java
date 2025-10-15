package com.deliveranything.domain.user.profile.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 배송지 추가 요청 DTO
 */
public record AddressCreateRequest(
    @NotBlank(message = "배송지 이름은 필수 입력 사항입니다.")
    @Size(max = 100, message = "배송지 이름은 100자 이하로 입력해주세요.")
    String addressName,

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    @Size(max = 300, message = "주소는 300자 이하로 입력해주세요.")
    String address,

    @NotNull(message = "위도는 필수 입력 사항입니다.")
    Double latitude,

    @NotNull(message = "경도는 필수 입력 사항입니다.")
    Double longitude
) {

}