package com.deliveranything.domain.settlement.dto.projection;

public record SettlementSummaryProjection(
    Long totalTransactionCount,
    Long weeklyTransactionCount,
    Long weeklySettledAmount,
    Long monthlySettledAmount,
    Long totalSettledAmount
) {

}
