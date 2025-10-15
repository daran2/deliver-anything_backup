package com.deliveranything.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentCancelResponse(
    String paymentKey,
    String orderId,
    List<CancelInfo> cancels
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CancelInfo(
      Long cancelAmount
  ) {

  }
}
