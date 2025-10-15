package com.deliveranything.domain.media.dto;

public record GeneratePresignedUrlResponse(
    String presignedUrl,
    String resourceKey
) {

}
