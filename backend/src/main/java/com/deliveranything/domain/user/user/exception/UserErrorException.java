package com.deliveranything.domain.user.user.exception;

import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserErrorException extends CustomException {

  public UserErrorException(ErrorCode errorCode) {
    super(errorCode);
  }
}
