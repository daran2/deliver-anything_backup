package com.deliveranything.domain.notification.scheduler;

import com.deliveranything.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseHeartbeatScheduler {

  private final EmitterRepository emitterRepository;

  @Scheduled(fixedRate = 30_000)
  public void sendHeartbeat() {
    emitterRepository.getAllEmitters().forEach((profileId, deviceEmitters) -> {
      deviceEmitters.forEach((deviceId, emitter) -> {
        try {
          emitter.send(SseEmitter.event()
              .name("heartbeat")
              .data("ping"));
        } catch (Exception e) {
          log.warn("Heartbeat failed for profileId {}, deviceId {}: {}. Completing emitter.",
              profileId, deviceId, e.getMessage());
          emitter.complete();
        }
      });
    });
  }
}