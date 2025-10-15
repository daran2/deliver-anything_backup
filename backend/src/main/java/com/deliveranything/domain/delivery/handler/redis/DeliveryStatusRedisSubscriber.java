package com.deliveranything.domain.delivery.handler.redis;

import com.deliveranything.domain.delivery.entity.Delivery;
import com.deliveranything.domain.delivery.event.dto.DeliveryStatusEvent;
import com.deliveranything.domain.delivery.repository.DeliveryRepository;
import com.deliveranything.domain.notification.subscriber.delivery.DeliveryStatusNotifier;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeliveryStatusRedisSubscriber implements MessageListener {

  private static final String CHANNEL = "delivery-status-events";

  private final ObjectMapper objectMapper;
  private final DeliveryStatusNotifier deliveryStatusNotifier;
  private final RedisMessageListenerContainer container;
  private final DeliveryRepository deliveryRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @PostConstruct
  public void subscribe() {
    container.addMessageListener(this, new PatternTopic(CHANNEL));
  }

  @Override
  @Transactional
  public void onMessage(Message message, byte[] pattern) {
    try {
      String body = new String(message.getBody());
      DeliveryStatusEvent event = objectMapper.readValue(body, DeliveryStatusEvent.class);

      // 1️⃣ 상태 변경 처리
      handleStatusChange(event);
      // 2️⃣ SSE 알림 전송
      deliveryStatusNotifier.publish(event);

    } catch (Exception e) {
      throw new CustomException(ErrorCode.REDIS_MESSAGE_PROCESSING_ERROR);
    }
  }

  // 상태 변경 처리
  private void handleStatusChange(DeliveryStatusEvent event) {
    Delivery delivery = deliveryRepository.findById(event.deliveryId())
        .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

    // 상태 업데이트
    delivery.updateStatus(event.nextStatus());

    // 특정 상태에 따른 추가 처리 (시작/완료 시간 기록)
    switch (event.nextStatus()) {
      case IN_PROGRESS -> {
        delivery.updateStartedAt(LocalDateTime.now());
      }
      case COMPLETED -> {
        delivery.updateCompletedAt(LocalDateTime.now());
      }
    }

    // Redis 캐시 갱신
    redisTemplate.opsForValue().set("delivery:" + event.deliveryId(), delivery);
  }
}