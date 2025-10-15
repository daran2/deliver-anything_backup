package com.deliveranything.domain.media.service;

import com.deliveranything.domain.media.dto.GeneratePresignedUrlRequest;
import com.deliveranything.domain.media.dto.GeneratePresignedUrlResponse;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Presigner s3Presigner;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public GeneratePresignedUrlResponse generatePresignedPutUrl(GeneratePresignedUrlRequest request) {
    String resourceKey = request.domain().getPath() + "/" + UUID.randomUUID() + "-" + request.fileName();

    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(resourceKey)
        .contentType(request.contentType())
        .build();

    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .putObjectRequest(objectRequest)
        .build();

    String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

    return new GeneratePresignedUrlResponse(presignedUrl, resourceKey);
  }

//    public String generatePresignedGetUrl(String resourceKey) {
//        if (resourceKey == null || resourceKey.isBlank()) {
//            return null;
//        }
//
//        GetObjectRequest objectRequest = GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(resourceKey)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .getObjectRequest(objectRequest)
//                .build();
//
//        return s3Presigner.presignGetObject(presignRequest).url().toString();
//    }
}
