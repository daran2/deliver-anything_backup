package com.deliveranything.global.security.expression;

import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import com.deliveranything.global.security.auth.SecurityUser;
import org.springframework.stereotype.Component;

@Component
public class ProfileSecurity {

  private SecurityUser getSecurityUser(Object principal) {
    if (principal == null) {
      throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
    }
    if (!(principal instanceof SecurityUser securityUser)) {
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }
    return securityUser;
  }

  public boolean isSeller(Object principal) {
    SecurityUser securityUser = getSecurityUser(principal);

    if (!securityUser.hasActiveProfile()) {
      throw new CustomException(ErrorCode.PROFILE_REQUIRED);
    }

    if (!securityUser.isSellerActive()) {
      throw new CustomException(ErrorCode.PROFILE_NOT_ALLOWED);
    }
    return true;
  }

  public boolean isCustomer(Object principal) {
    SecurityUser securityUser = getSecurityUser(principal);

    if (!securityUser.hasActiveProfile()) {

      throw new CustomException(ErrorCode.PROFILE_REQUIRED);
    }

    if (!securityUser.isCustomerActive()) {
      throw new CustomException(ErrorCode.PROFILE_NOT_ALLOWED);
    }
    return true;
  }

  public boolean isRider(Object principal) {
    SecurityUser securityUser = getSecurityUser(principal);

    if (!securityUser.hasActiveProfile()) {
      throw new CustomException(ErrorCode.PROFILE_REQUIRED);
    }

    if (!securityUser.isRiderActive()) {
      throw new CustomException(ErrorCode.PROFILE_NOT_ALLOWED);
    }
    return true;
  }
}