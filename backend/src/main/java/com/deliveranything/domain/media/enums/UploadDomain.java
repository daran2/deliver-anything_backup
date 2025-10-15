package com.deliveranything.domain.media.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadDomain {
  USER_PROFILE("user-profile"),
  STORE("store"),
  PRODUCT("product"),
  REVIEW("review");

  private final String path;
}