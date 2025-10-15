package com.deliveranything.domain.store.category.controller;

import com.deliveranything.domain.store.category.dto.StoreCategoryResponse;
import com.deliveranything.domain.store.category.service.StoreCategoryService;
import com.deliveranything.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상점 카테고리 관련 API", description = "상점 카테고리 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/store-categories")
public class StoreCategoryController {

  private final StoreCategoryService storeCategoryService;

  @Operation(summary = "상점 카테고리 목록 조회", description = "모든 상점 카테고리 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<StoreCategoryResponse>>> getCategories() {
    return ResponseEntity.ok(ApiResponse.success(storeCategoryService.getAllCategories()));
  }
}
