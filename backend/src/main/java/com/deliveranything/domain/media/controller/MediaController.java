package com.deliveranything.domain.media.controller;

import com.deliveranything.domain.media.dto.GeneratePresignedUrlRequest;
import com.deliveranything.domain.media.dto.GeneratePresignedUrlResponse;
import com.deliveranything.domain.media.service.S3Service;
import com.deliveranything.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "미디어(이미지) 관련 API", description = "파일 업로드 및 미디어 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

  private final S3Service s3Service;

  @Operation(summary = "파일 업로드를 위한 Pre-signed URL 생성", description = "S3에 파일을 업로드하기 위한 임시 URL(Pre-signed URL)을 생성합니다.")
  @PostMapping("/presigned-url")
  public ResponseEntity<ApiResponse<GeneratePresignedUrlResponse>> generatePresignedUrl(
      @Valid @RequestBody GeneratePresignedUrlRequest request
  ) {
    GeneratePresignedUrlResponse response = s3Service.generatePresignedPutUrl(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}