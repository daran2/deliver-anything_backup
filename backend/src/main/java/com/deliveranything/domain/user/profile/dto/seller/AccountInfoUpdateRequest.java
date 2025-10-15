package com.deliveranything.domain.user.profile.dto.seller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 정산 계좌 정보 수정 요청 DTO
 */
public record AccountInfoUpdateRequest(
    @Size(max = 50, message = "은행명은 50자 이하로 입력해주세요.")
    String bankName,

    @Pattern(regexp = "^\\d{10,14}$", message = "계좌번호는 10~14자리 숫자여야 합니다.")
    String accountNumber,

    @Size(max = 50, message = "예금주는 50자 이하로 입력해주세요.")
    String accountHolder
) {
  // 최소 하나의 필드는 입력되어야 함을 컨트롤러에서 검증
}