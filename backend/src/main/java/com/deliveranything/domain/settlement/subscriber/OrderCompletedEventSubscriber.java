package com.deliveranything.domain.settlement.subscriber;

import com.deliveranything.domain.order.event.OrderCompletedEvent;
import com.deliveranything.domain.settlement.service.SettlementDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompletedEventSubscriber implements MessageListener {

  private final RedisMessageListenerContainer container;
  private final ObjectMapper objectMapper;
  private final SettlementDetailService settlementDetailService;

  @PostConstruct
  public void registerListener() {
    container.addMessageListener(this, new ChannelTopic("order-completed-event"));
  }

  @Override
  public void onMessage(@NonNull Message message, byte[] pattern) {
    try {
      OrderCompletedEvent event = objectMapper.readValue(message.getBody(),
          OrderCompletedEvent.class);
      settlementDetailService.createRiderSettlement(event.orderId(), event.riderProfileId(),
          event.deliveryPrice());
      settlementDetailService.createSellerSettlement(event.orderId(), event.sellerProfileId(),
          event.storePrice());
    } catch (Exception e) {
      log.error("Failed to process order completed event from Redis", e);
    }
  }
}
