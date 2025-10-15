package com.deliveranything.domain.notification.subscriber.delivery;

import com.deliveranything.domain.delivery.event.dto.OrderStatusUpdateEvent;
import com.deliveranything.domain.notification.enums.NotificationMessage;
import com.deliveranything.domain.notification.enums.NotificationType;
import com.deliveranything.domain.notification.service.NotificationService;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDeliveryStatusNotifier {

  private final ObjectMapper objectMapper;
  private final NotificationService notificationService;

  public void publish(Long profileId, OrderStatusUpdateEvent event) {
    try {
      notificationService.sendNotification(
          profileId,
          NotificationType.RIDER_DECISION,
          NotificationMessage.RIDER_DECISION.getMessage(),
          objectMapper.writeValueAsString(event)
      );
    } catch (Exception e) {
      throw new CustomException(ErrorCode.SSE_SUBSCRIBE_UNAVAILABLE);
    }

  }
}
