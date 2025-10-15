package com.deliveranything.domain.settlement.dto;

import com.deliveranything.domain.settlement.entity.SettlementDetail;
import java.util.List;

public record UnsettledResponse(
    Long scheduledSettleAmount,
    Integer scheduledTransactionCount
) {

  public static UnsettledResponse from(List<SettlementDetail> settlementDetails) {
    return new UnsettledResponse(
        settlementDetails.stream().mapToLong(SettlementDetail::getTargetAmount).sum(),
        settlementDetails.size()
    );
  }
}
