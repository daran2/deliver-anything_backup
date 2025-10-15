package com.deliveranything.domain.review.consumer;

import com.deliveranything.domain.review.dto.ReviewLikeEvent;
import com.deliveranything.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewConsumer {

  private final ReviewRepository reviewRepository;

  @Transactional
  public void handleLikeEvent(ReviewLikeEvent event) {
    try {
      reviewRepository.updateLikeCount(event.reviewId(), event.likeCount());
      log.info("리뷰 {} → 좋아요 수 {} DB 반영 완료",
          event.reviewId(), event.likeCount());
    } catch (Exception e) {
      log.error("리뷰 {} 좋아요 {} DB 반영 실패",
          event.reviewId(), event.likeCount(), e);
    }
  }
}
