package com.deliveranything.domain.search.store.subscriber;

import com.deliveranything.domain.product.product.event.ProductKeywordsChangedEvent;
import com.deliveranything.domain.search.store.service.StoreKeywordSyncService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventSubscriber implements MessageListener {

  private final RedisMessageListenerContainer container;
  private final ObjectMapper objectMapper;
  private final StoreKeywordSyncService storeKeywordSyncService;

  @PostConstruct
  public void registerListener() {
    container.addMessageListener(this, new PatternTopic("product-keywords-events"));
  }

  @Override
  public void onMessage(@NonNull Message message, byte[] pattern) {
    try {
      String json = new String(message.getBody());
      ProductKeywordsChangedEvent event = objectMapper.readValue(json, ProductKeywordsChangedEvent.class);

      storeKeywordSyncService.syncKeywords(event.storeId());

    } catch (Exception e) {
      log.error("Failed to process product keyword event from Redis", e);
    }
  }
}
