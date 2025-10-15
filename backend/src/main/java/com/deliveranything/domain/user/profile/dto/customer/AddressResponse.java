package com.deliveranything.domain.user.profile.dto.customer;

import com.deliveranything.domain.user.profile.entity.CustomerAddress;
import lombok.Builder;

/**
 * 배송지 조회 응답 DTO
 */
@Builder
public record AddressResponse(
    Long addressId,
    String addressName,
    String address,
    Double latitude,
    Double longitude,
    boolean isDefault
) {

  /**
   * CustomerAddress 엔티티로부터 DTO 생성
   */
  public static AddressResponse from(CustomerAddress customerAddress) {
    if (customerAddress == null) {
      return null;
    }

    return AddressResponse.builder()
        .addressId(customerAddress.getId())
        .addressName(customerAddress.getAddressName())
        .address(customerAddress.getAddress())
        .latitude(customerAddress.getLocation().getY())
        .longitude(customerAddress.getLocation().getX())
        .isDefault(customerAddress.isDefault())
        .build();
  }
}