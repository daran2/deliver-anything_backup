package com.deliveranything.domain.delivery.controller;

import com.deliveranything.domain.delivery.dto.RiderLocationDto;
import com.deliveranything.domain.delivery.service.RiderLocationService;
import com.deliveranything.domain.delivery.websocket.RiderWebSocketPublisher;
import com.deliveranything.global.security.auth.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiderLocationController {

  private final RiderLocationService riderLocationService;
  private final RiderWebSocketPublisher webSocketPublisher;

  @MessageMapping("/location") // 클라이언트에서 /app/location 으로 전송
  public void updateLocation(
      @Valid @Payload RiderLocationDto location,
      @AuthenticationPrincipal SecurityUser user
  ) {
    riderLocationService.saveRiderLocation(user.getCurrentActiveProfileIdSafe(), location);
    webSocketPublisher.publishLocation(user.getCurrentActiveProfileIdSafe(), location);
    // 서버에서 클라이언트로 /topic/rider/location 으로 전송
  }
}
