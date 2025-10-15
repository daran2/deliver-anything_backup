package com.deliveranything.domain.settlement.dto.projection;

import java.time.LocalDate;

public record SettlementProjection(
    Long targetTotalAmount,
    Long totalPlatformFee,
    Long settledAmount,
    Long transactionCount,
    LocalDate minDate,
    LocalDate maxDate
) {

}
