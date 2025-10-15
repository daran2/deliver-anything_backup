package com.deliveranything.domain.delivery.event.event.redis;

import com.deliveranything.domain.delivery.event.dto.OrderStatusUpdateEvent;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDeliveryStatusRedisPublisher {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;

  private static final String CHANNEL = "order-delivery-status";

  public void publish(OrderStatusUpdateEvent event) {
    try {
      String message = objectMapper.writeValueAsString(event);
      redisTemplate.convertAndSend(CHANNEL, message);
    } catch (JsonProcessingException e) {
      throw new CustomException(ErrorCode.REDIS_MESSAGE_PROCESSING_ERROR);
    }
  }
}
