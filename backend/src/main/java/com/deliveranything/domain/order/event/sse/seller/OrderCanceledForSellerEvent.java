package com.deliveranything.domain.order.event.sse.seller;

import com.deliveranything.domain.order.entity.Order;

public record OrderCanceledForSellerEvent(Long sellerId, Long orderId) {

  public static OrderCanceledForSellerEvent fromOrder(Order order) {
    return new OrderCanceledForSellerEvent(order.getStore().getSellerProfileId(), order.getId());
  }
}
