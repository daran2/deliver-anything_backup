package com.deliveranything.domain.order.dto;

import com.deliveranything.domain.order.entity.Order;
import com.deliveranything.domain.order.enums.OrderStatus;
import com.deliveranything.domain.order.event.dto.OrderItemInfo;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    List<OrderItemInfo> orderItems,
    OrderStatus status,
    String merchantId,
    String storeName,
    String address,
    String riderNote,
    String storeNote,
    Long totalPrice,
    Long storePrice,
    Long deliveryPrice,
    LocalDateTime createdAt
) {

  public static OrderResponse from(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getOrderItems().stream().map(OrderItemInfo::fromOrderItem).toList(),
        order.getStatus(),
        order.getMerchantId(),
        order.getStore().getName(),
        order.getAddress(),
        order.getRiderNote(),
        order.getStoreNote(),
        order.getTotalPrice(),
        order.getStorePrice(),
        order.getDeliveryPrice(),
        order.getCreatedAt()
    );
  }
}
