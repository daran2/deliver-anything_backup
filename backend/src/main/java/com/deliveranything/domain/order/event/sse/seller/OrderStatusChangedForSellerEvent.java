package com.deliveranything.domain.order.event.sse.seller;

import com.deliveranything.domain.order.entity.Order;
import com.deliveranything.domain.order.enums.OrderStatus;

public record OrderStatusChangedForSellerEvent(
    Long orderId,
    Long sellerId,
    OrderStatus orderStatus
) {

  public static OrderStatusChangedForSellerEvent fromOrder(Order order) {
    return new OrderStatusChangedForSellerEvent(
        order.getId(),
        order.getStore().getSellerProfileId(),
        order.getStatus()
    );
  }
}
