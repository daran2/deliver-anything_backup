package com.deliveranything.domain.delivery.handler;

import com.deliveranything.domain.delivery.event.dto.DeliveryStatusEvent;
import com.deliveranything.domain.delivery.event.dto.OrderStatusUpdateEvent;
import com.deliveranything.domain.delivery.event.event.redis.DeliveryStatusRedisPublisher;
import com.deliveranything.domain.delivery.event.event.redis.OrderDeliveryStatusRedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryEventHandler {

  private final DeliveryStatusRedisPublisher deliveryStatusRedisPublisher;
  private final OrderDeliveryStatusRedisPublisher orderDeliveryStatusRedisPublisher; // Kafka → Redis 변경

  // 배달 상태 변경 이벤트 발행
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleDeliveryStatus(DeliveryStatusEvent orderEvent) {
    deliveryStatusRedisPublisher.publish(orderEvent);
  }

  // 배달 수락-거절 이벤트 발행
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleOrderDeliveryStatus(OrderStatusUpdateEvent orderEvent) {
    orderDeliveryStatusRedisPublisher.publish(orderEvent);
  }
}