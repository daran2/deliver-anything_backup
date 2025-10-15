package com.deliveranything.domain.user.profile.entity;

import com.deliveranything.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 공통 속성을 담는 상위 클래스 (테이블로 생성되지 않음)
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseProfile extends BaseEntity {

  @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(50)")
  private String nickname;

  @Column(name = "profile_image_url", columnDefinition = "VARCHAR(500)")
  private String profileImageUrl;


  public BaseProfile(String nickname, String profileImageUrl) {
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }

  // 기타 공통 메서드
  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

}
