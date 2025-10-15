package com.deliveranything.domain.review.enums;

public enum LikeAction {
  LIKE(1, true),     // ZINCRBY +1
  UNLIKE(-1, false); // ZINCRBY -1

  private final int increment;
  private final boolean likedByMe;

  LikeAction(int increment, boolean likedByMe) {
    this.increment = increment;
    this.likedByMe = likedByMe;
  }

  public int getIncrement() {
    return increment;
  }

  public boolean isLikedByMe() {
    return likedByMe;
  }
}
