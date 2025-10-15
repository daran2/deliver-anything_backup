package com.deliveranything.domain.notification.subscriber.delivery;

import com.deliveranything.domain.delivery.event.dto.DeliveryStatusEvent;
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
public class DeliveryStatusNotifier {

  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  // 프로필 단위 전체 전송 (해당 프로필의 모든 emitter로 전송)
  public void publish(DeliveryStatusEvent event) {
    try {
      if (event.customerProfileId() != null) {
        notificationService.sendNotification(
            event.customerProfileId(),
            NotificationType.RIDER_STATUS_CHANGED,
            NotificationMessage.RIDER_STATUS_CHANGED.getMessage(),
            objectMapper.writeValueAsString(event));
      }
      if (event.sellerProfileId() != null) {
        notificationService.sendNotification(
            event.sellerProfileId(),
            NotificationType.RIDER_STATUS_CHANGED,
            NotificationMessage.RIDER_STATUS_CHANGED.getMessage(),
            objectMapper.writeValueAsString(event));
      }
      if (event.riderProfileId() != null) {
        notificationService.sendNotification(
            event.riderProfileId(),
            NotificationType.RIDER_STATUS_CHANGED,
            NotificationMessage.RIDER_STATUS_CHANGED.getMessage(),
            objectMapper.writeValueAsString(event));
      }
    } catch (Exception e) {
      throw new CustomException(ErrorCode.SSE_SUBSCRIBE_UNAVAILABLE);
    }

  }
}
