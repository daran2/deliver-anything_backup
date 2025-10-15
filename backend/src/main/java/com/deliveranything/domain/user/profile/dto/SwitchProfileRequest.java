package com.deliveranything.domain.user.profile.dto;

import com.deliveranything.domain.user.profile.enums.ProfileType;
import jakarta.validation.constraints.NotNull;

/**
 * 프로필 전환 요청 DTO
 */
public record SwitchProfileRequest(
    @NotNull(message = "전환할 프로필 타입은 필수입니다.")
    ProfileType targetProfileType
) {

}