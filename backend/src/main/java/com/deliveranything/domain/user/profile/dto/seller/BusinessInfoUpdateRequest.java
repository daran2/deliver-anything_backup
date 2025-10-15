package com.deliveranything.domain.user.profile.dto.seller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 사업자 정보 수정 요청 DTO
 */
public record BusinessInfoUpdateRequest(
    @Size(max = 100, message = "사업자명은 100자 이하로 입력해주세요.")
    String businessName,

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    String businessPhoneNumber
) {
  // 최소 하나의 필드는 입력되어야 함을 컨트롤러에서 검증
}