package com.deliveranything.domain.media.dto;

import com.deliveranything.domain.media.enums.UploadDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "S3 Presigned URL 생성 요청")
public record GeneratePresignedUrlRequest(
    @Schema(description = "파일 이름")
    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    @Schema(description = "업로드 도메인. 사용 가능한 값: USER_PROFILE, STORE, PRODUCT, REVIEW")
    @NotNull(message = "도메인은 필수입니다.")
    UploadDomain domain,

    @Schema(description = "파일 Content-Type")
    @NotBlank(message = "Content-Type은 필수입니다.")
    String contentType
) {

}