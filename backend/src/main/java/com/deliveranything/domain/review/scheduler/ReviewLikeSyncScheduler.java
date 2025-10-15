package com.deliveranything.domain.review.scheduler;

import com.deliveranything.domain.review.dto.ReviewLikeEvent;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ReviewLikeSyncScheduler {

  private final RedisTemplate<String, Object> redisTemplate;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ReviewLikeSyncScheduler(RedisTemplate<String, Object> redisTemplate,
      KafkaTemplate<String, Object> kafkaTemplate) {
    this.redisTemplate = redisTemplate;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Scheduled(fixedRate = 5 * 60 * 1000)
  @Transactional
  public void syncLikesToDb() {
    String reviewSortedKey = "review:likes";

    //모든 리뷰 ID와 score 가져오기
    Set<ZSetOperations.TypedTuple<Object>> tuples =
        redisTemplate.opsForZSet().rangeWithScores(reviewSortedKey, 0, -1);

    if (tuples != null) {
      for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
        Long reviewId = (Long) tuple.getValue();
        Double score = tuple.getScore();

        if (score == null) {
          log.warn("리뷰 {}: 좋아요 score가 없습니다.", reviewId);
          continue;
        }
        int likeCount = score.intValue();

        ReviewLikeEvent event = new ReviewLikeEvent(reviewId, likeCount);
        kafkaTemplate.send("review-like-topic", reviewId.toString(), event);
        log.info("리뷰 {}: 좋아요 수 이벤트 발행 -> {}", reviewId, likeCount);
      }
    }
  }

}
