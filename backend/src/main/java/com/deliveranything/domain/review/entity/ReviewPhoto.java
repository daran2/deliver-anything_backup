package com.deliveranything.domain.review.entity;

import com.deliveranything.global.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "review_photos")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhoto extends BaseEntity {

  @Schema(description = "리뷰 객체")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id", nullable = false)
  private Review review;

  @Schema(description = "사진 URL")
  @Column(name = "photo_url")
  private String photoUrl;

  @LastModifiedDate
  @Schema(description = "수정 일시")
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  //========================생성 메소드===========================
  @Builder
  public ReviewPhoto(
      Review review,
      String photoUrl) {
    this.review = review;
    this.photoUrl = photoUrl;
  }
}
