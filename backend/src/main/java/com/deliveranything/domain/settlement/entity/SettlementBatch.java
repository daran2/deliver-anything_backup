package com.deliveranything.domain.settlement.entity;

import com.deliveranything.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "settlement_batches")
public class SettlementBatch extends BaseEntity {

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  private Long targetTotalAmount;

  @Column(nullable = false)
  private Integer transactionCount;

  @Column(nullable = false)
  private Long totalPlatformFee;

  @Column(nullable = false)
  private Long settledAmount;

  @Column(nullable = false)
  private LocalDate settlementDate;

  @Builder
  public SettlementBatch(Long targetId, Long targetTotalAmount, Integer transactionCount,
      Long totalPlatformFee, Long settledAmount, LocalDate settlementDate) {
    this.targetId = targetId;
    this.targetTotalAmount = targetTotalAmount;
    this.transactionCount = transactionCount;
    this.totalPlatformFee = totalPlatformFee;
    this.settledAmount = settledAmount;
    this.settlementDate = settlementDate;
  }
}
