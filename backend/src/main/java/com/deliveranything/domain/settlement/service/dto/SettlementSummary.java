package com.deliveranything.domain.settlement.service.dto;

public record SettlementSummary(
    Long totalTargetAmount,
    Long totalPlatformFee,
    Integer transactionCount
) {

}