package com.deliveranything.domain.settlement.dto;

import com.deliveranything.domain.settlement.entity.SettlementDetail;
import com.deliveranything.domain.settlement.enums.SettlementStatus;

public record SettlementDetailResponse(
    Long orderId,
    SettlementStatus settlementStatus
) {

  public static SettlementDetailResponse from(SettlementDetail settlementDetail) {
    return new SettlementDetailResponse(
        settlementDetail.getOrderId(),
        settlementDetail.getStatus()
    );
  }
}
