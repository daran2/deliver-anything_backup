package com.deliveranything.domain.settlement.dto;

import com.deliveranything.domain.settlement.dto.projection.SettlementSummaryProjection;

public record SummaryResponse(
    Integer totalTransactionCount,
    Integer weeklyTransactionCount,
    Long weeklySettledAmount,
    Long monthlySettledAmount,
    Long totalSettledAmount,
    Long scheduledSettleAmount
) {

  public static SummaryResponse fromSettledAndUnsettled(
      SettlementSummaryProjection ssp,
      UnsettledResponse ur
  ) {
    return new SummaryResponse(
        ssp.totalTransactionCount().intValue() + ur.scheduledTransactionCount(),
        ssp.weeklyTransactionCount().intValue() + ur.scheduledTransactionCount(),
        ssp.weeklySettledAmount(),
        ssp.monthlySettledAmount(),
        ssp.totalSettledAmount(),
        ur.scheduledSettleAmount()
    );
  }
}
