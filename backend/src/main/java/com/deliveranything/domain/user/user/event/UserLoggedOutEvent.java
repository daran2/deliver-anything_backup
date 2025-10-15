package com.deliveranything.domain.user.user.event;

/**
 * 사용자가 로그아웃했을 때 발행되는 이벤트
 *
 * @param profileId 로그아웃한 사용자의 프로필 ID
 * @param deviceId  로그아웃한 기기의 ID
 */
public record UserLoggedOutEvent(
    Long profileId,
    String deviceId
) {

}
