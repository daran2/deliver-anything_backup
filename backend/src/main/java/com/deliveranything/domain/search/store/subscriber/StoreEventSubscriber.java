package com.deliveranything.domain.search.store.subscriber;

import com.deliveranything.domain.search.store.service.StoreSyncService;
import com.deliveranything.domain.store.store.event.StoreDeletedEvent;
import com.deliveranything.domain.store.store.event.StoreEventType;
import com.deliveranything.domain.store.store.event.StoreSavedEvent;
import com.fasterxml.jackson.databind.JsonNode;
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
public class StoreEventSubscriber implements MessageListener {

  private final RedisMessageListenerContainer container;
  private final ObjectMapper objectMapper;
  private final StoreSyncService storeSyncService;

  @PostConstruct
  public void registerListener() {
    container.addMessageListener(this, new PatternTopic("store-events"));
  }

  @Override
  public void onMessage(@NonNull Message message, byte[] pattern) {
    try {
      String json = new String(message.getBody());
      JsonNode node = objectMapper.readTree(json);
      StoreEventType type = StoreEventType.valueOf(node.get("type").asText());

      switch (type) {
        case SAVED -> {
          StoreSavedEvent event = objectMapper.treeToValue(node, StoreSavedEvent.class);
          storeSyncService.handleSaved(event.storeId());
        }
        case DELETED -> {
          StoreDeletedEvent event = objectMapper.treeToValue(node, StoreDeletedEvent.class);
          storeSyncService.handleDeleted(event.storeId());
        }
      }
    } catch (Exception e) {
      log.error("Failed to process store event from Redis: {}", new String(message.getBody()), e);
    }
  }
}