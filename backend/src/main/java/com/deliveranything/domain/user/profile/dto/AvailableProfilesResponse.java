package com.deliveranything.domain.user.profile.dto;

import com.deliveranything.domain.user.profile.enums.ProfileType;
import java.util.List;
import lombok.Builder;

@Builder
public record AvailableProfilesResponse(
    Long userId,
    List<ProfileType> availableProfiles,
    ProfileType currentActiveProfile
) {

}