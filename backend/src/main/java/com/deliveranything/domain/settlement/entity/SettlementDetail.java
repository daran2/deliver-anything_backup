package com.deliveranything.domain.settlement.entity;

import com.deliveranything.domain.settlement.enums.SettlementStatus;
import com.deliveranything.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "settlement_details")
public class SettlementDetail extends BaseEntity {

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  private Long targetAmount;

  @Column(nullable = false)
  private Long platformFee;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SettlementStatus status;

  private Long batchId;

  @Builder
  public SettlementDetail(Long orderId, Long targetId, Long targetAmount, Long platformFee) {
    this.orderId = orderId;
    this.targetId = targetId;
    this.targetAmount = targetAmount;
    this.platformFee = platformFee;
    this.status = SettlementStatus.PENDING;
  }

  public void process(Long batchId) {
    this.batchId = batchId;
    this.status = SettlementStatus.COMPLETED;
  }
}
