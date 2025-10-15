package com.deliveranything.domain.delivery.event.event.redis;

import com.deliveranything.domain.delivery.event.dto.DeliveryStatusEvent;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryStatusRedisPublisher {

  // 배달 상태 변경 이벤트 채널
  private static final String CHANNEL = "delivery-status-events";
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  public void publish(DeliveryStatusEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      redisTemplate.convertAndSend(CHANNEL, payload);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.REDIS_MESSAGE_PROCESSING_ERROR);
    }
  }
}
