package com.deliveranything.domain.store.store.controller;

import com.deliveranything.domain.store.store.dto.StoreCreateRequest;
import com.deliveranything.domain.store.store.dto.StoreResponse;
import com.deliveranything.domain.store.store.dto.StoreUpdateRequest;
import com.deliveranything.domain.store.store.service.StoreService;
import com.deliveranything.global.common.ApiResponse;
import com.deliveranything.global.security.auth.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상점 관련 API", description = "상점 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {

  private final StoreService storeService;

  @Operation(summary = "상점 생성", description = "새로운 상점을 생성합니다, 상점은 하나의 SellerProfile 당 1개 생성 가능합니다.")
  @PostMapping
  @PreAuthorize("@profileSecurity.isSeller(#securityUser)")
  public ResponseEntity<ApiResponse<Long>> createStore(
      @Valid @RequestBody StoreCreateRequest request,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    Long storeId = storeService.createStore(request, securityUser.getCurrentActiveProfile().getId());
    return ResponseEntity.created(URI.create("/api/v1/stores/" + storeId))
        .body(ApiResponse.success(storeId));
  }

  @Operation(summary = "상점 단건 조회", description = "특정 상점의 정보를 조회합니다.")
  @GetMapping("/{storeId}")
  public ResponseEntity<ApiResponse<StoreResponse>> getStore(
      @PathVariable Long storeId
  ) {
    return ResponseEntity.ok(ApiResponse.success(storeService.getStore(storeId)));
  }

  @Operation(summary = "상점 정보 수정", description = "특정 상점의 정보를 수정합니다, 본인의 상점 정보만 수정이 가능합니다.")
  @PutMapping("/{storeId}")
  @PreAuthorize("@profileSecurity.isSeller(#securityUser) and @storeSecurity.isOwner(#storeId, #securityUser)")
  public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
      @PathVariable Long storeId,
      @Valid @RequestBody StoreUpdateRequest request,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.ok(ApiResponse.success(storeService.updateStore(storeId, request)));
  }

  @Operation(summary = "상점 삭제", description = "특정 상점을 삭제합니다, 본인의 상점만 삭제가 가능합니다.")
  @DeleteMapping("/{storeId}")
  @PreAuthorize("@profileSecurity.isSeller(#securityUser) and @storeSecurity.isOwner(#storeId, #securityUser)")
  public ResponseEntity<ApiResponse<Void>> deleteStore(
      @PathVariable Long storeId,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    storeService.deleteStore(storeId);
    return ResponseEntity.status(204).body(ApiResponse.success());
  }

  @Operation(summary = "상점 영업상태 변경", description = "상점의 영업 상태(OPEN/CLOSED)를 변경합니다.")
  @PostMapping("/{storeId}/toggle-status")
  @PreAuthorize("@profileSecurity.isSeller(#securityUser) and @storeSecurity.isOwner(#storeId, #securityUser)")
  public ResponseEntity<ApiResponse<StoreResponse>> toggleStoreStatus(
      @PathVariable Long storeId,
      @AuthenticationPrincipal SecurityUser securityUser
  ) {
    return ResponseEntity.ok(ApiResponse.success(storeService.toggleStoreStatus(storeId)));
  }
}
