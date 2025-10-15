package com.deliveranything.domain.delivery.controller;

import com.deliveranything.domain.delivery.dto.request.DeliveryAreaRequestDto;
import com.deliveranything.domain.delivery.dto.request.DeliveryStatusRequestDto;
import com.deliveranything.domain.delivery.dto.request.RiderDecisionRequestDto;
import com.deliveranything.domain.delivery.dto.request.RiderToggleStatusRequestDto;
import com.deliveranything.domain.delivery.dto.response.CurrentDeliveringDetailsDto;
import com.deliveranything.domain.delivery.dto.response.CurrentDeliveringResponseDto;
import com.deliveranything.domain.delivery.dto.response.DeliveredSummaryResponseDto;
import com.deliveranything.domain.delivery.dto.response.TodayDeliveringResponseDto;
import com.deliveranything.domain.delivery.enums.DeliveryStatus;
import com.deliveranything.domain.delivery.service.DeliveryService;
import com.deliveranything.global.common.ApiResponse;
import com.deliveranything.global.security.auth.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "Delivery", description = "배달 API")
public class DeliveryController {

  private final DeliveryService deliveryService;

  @PatchMapping("/status")
  @Operation(summary = "라이더 토글 전환", description = "라이더 토글 전환으로 상태를 전환합니다.")
  public ResponseEntity<ApiResponse<Void>> updateRiderStatus(
      @AuthenticationPrincipal SecurityUser user,
      @Valid @RequestBody RiderToggleStatusRequestDto riderStatusRequestDto
  ) {
    deliveryService.updateRiderStatus(user.getCurrentActiveProfileIdSafe(),
        riderStatusRequestDto);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @PostMapping("/area")
  @Operation(summary = "배달 가능 지역 설정",
      description = "배달 가능 지역을 설정합니다 (현재는 1군데만, 자유로운 형식으로 가능).")
  public ResponseEntity<ApiResponse<Void>> updateDeliveryArea(
      @AuthenticationPrincipal SecurityUser user,
      @Valid @RequestBody DeliveryAreaRequestDto deliveryAreaRequestDto
  ) {
    deliveryService.updateDeliveryArea(user.getCurrentActiveProfileIdSafe(),
        deliveryAreaRequestDto);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @PatchMapping("/{deliveryId}/delivery-status")
  @Operation(summary = "배달 상태 변경", description = "배달 ID와 다음 상태를 받아 배달 상태를 변경합니다.")
  public ResponseEntity<Void> updateStatus(
      @PathVariable Long deliveryId,
      @Valid @RequestParam DeliveryStatusRequestDto next
  ) {
    deliveryService.changeDeliveryStatus(deliveryId, DeliveryStatus.valueOf(next.status()));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/decision")
  @Operation(summary = "라이더 배달 수락/거절 결정",
      description = "라이더가 배달 요청에 대해 수락 또는 거절을 결정합니다.")
  public ResponseEntity<ApiResponse<Void>> decideOrderDelivery(
      @Valid @RequestBody RiderDecisionRequestDto decisionRequestDto,
      @AuthenticationPrincipal SecurityUser user
  ) {
    deliveryService.publishRiderDecision(decisionRequestDto, user.getCurrentActiveProfileIdSafe());
    return ResponseEntity.ok(ApiResponse.success());
  }

  @GetMapping("/today")
  @Operation(summary = "오늘의 배달 내역 조회",
      description = "라이더의 오늘 배달 내역을 조회합니다.")
  public ResponseEntity<ApiResponse<TodayDeliveringResponseDto>> getTodayDeliveries(
      @AuthenticationPrincipal SecurityUser user
  ) {
    TodayDeliveringResponseDto response = deliveryService.getTodayDeliveringInfo(
        user.getCurrentActiveProfileIdSafe()
    );
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/in-progress")
  @Operation(summary = "진행 중인 배달 조회",
      description = "라이더의 진행 중인 배달 내역을 조회합니다." +
          " (동적 riderProfile ID 필요성으로 인해 클라이언트 측에서 1분에 한 번씩 폴링 권장)")
  public ResponseEntity<ApiResponse<List<CurrentDeliveringResponseDto>>> getInProgressDelivery(
      @AuthenticationPrincipal SecurityUser user
  ) {
    List<CurrentDeliveringResponseDto> response = deliveryService.getCurrentDeliveringInfo(
        user.getCurrentActiveProfileIdSafe()
    );
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/in-progress/{deliveryId}")
  @Operation(summary = "진행 중인 배달 단건 상세 조회",
      description = "라이더의 진행 중인 단일 배달 내역을 조회합니다." +
          " (동적 riderProfile ID 필요성으로 인해 클라이언트 측에서 1분에 한 번씩 폴링 권장)")
  public ResponseEntity<ApiResponse<CurrentDeliveringDetailsDto>> getInProgressDetailDelivery(
      @PathVariable Long deliveryId
  ) {
    CurrentDeliveringDetailsDto response = deliveryService.getCurrentDeliveringDetails(deliveryId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/total")
  @Operation(summary = "총 배달 내역 요약 조회 + 배달 완료 리스트 조회",
      description = "라이더의 총 배달 내역 요약과 배달 완료 리스트를 조회합니다. cursor 기반 페이징을 지원합니다."
          + " filter: LATEST(최신순), OLDEST(오래된순)")
  public ResponseEntity<ApiResponse<DeliveredSummaryResponseDto>> getTotalDeliveries(
      @AuthenticationPrincipal SecurityUser user,
      @RequestParam(required = false, defaultValue = "LATEST") String filter,
      @RequestParam(required = false) String cursor,
      @RequestParam(required = false, defaultValue = "10") Integer size
  ) {
    DeliveredSummaryResponseDto response = deliveryService.getDeliveredSummary(
        user.getCurrentActiveProfileIdSafe(),
        filter,
        cursor,
        size
    );
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
