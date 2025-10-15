package com.deliveranything.domain.settlement.service;

import com.deliveranything.domain.settlement.dto.SettlementDetailResponse;
import com.deliveranything.domain.settlement.dto.UnsettledResponse;
import com.deliveranything.domain.settlement.entity.SettlementDetail;
import com.deliveranything.domain.settlement.enums.SettlementStatus;
import com.deliveranything.domain.settlement.repository.SettlementDetailRepository;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SettlementDetailService {

  private final SettlementDetailRepository settlementDetailRepository;

  @Transactional
  public void createSellerSettlement(Long orderId, Long sellerProfileId, Long storePrice) {
    double PLATFORM_FEE_RATE = 0.08;
    long platformFee = (long) (storePrice * PLATFORM_FEE_RATE);

    SettlementDetail settlementDetail = SettlementDetail.builder()
        .orderId(orderId)
        .targetId(sellerProfileId)
        .targetAmount(storePrice - platformFee)
        .platformFee(platformFee)
        .build();

    settlementDetailRepository.save(settlementDetail);
  }

  @Transactional
  public void createRiderSettlement(Long orderId, Long riderProfileId, Long deliveryPrice) {
    SettlementDetail settlementDetail = SettlementDetail.builder()
        .orderId(orderId)
        .targetId(riderProfileId)
        .targetAmount(deliveryPrice)
        .platformFee(0L)
        .build();

    settlementDetailRepository.save(settlementDetail);
  }

  // 배달원이 배달한 주문의 정산 정보 조회
  @Transactional(readOnly = true)
  public SettlementDetailResponse getRiderSettlementDetail(Long orderId, Long riderProfileId) {
    return SettlementDetailResponse.from(
        settlementDetailRepository.findByOrderIdAndTargetId(orderId, riderProfileId)
            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_DETAIL_NOT_FOUND)));
  }

  // 금일 미정산 정보
  @Transactional(readOnly = true)
  public UnsettledResponse getUnsettledDetail(Long targetId) {
    LocalDate today = LocalDate.now();
    return UnsettledResponse.from(settlementDetailRepository.findAllUnsettledDetails(targetId,
        SettlementStatus.PENDING, today.atStartOfDay(), today.plusDays(1).atStartOfDay()));
  }

  @Transactional(readOnly = true)
  public List<SettlementDetail> getYesterdayUnsettledDetails() {
    LocalDate today = LocalDate.now();
    return settlementDetailRepository.findAllByStatusAndDateTime(SettlementStatus.PENDING,
        today.minusDays(1).atStartOfDay(), today.atStartOfDay());
  }
}
