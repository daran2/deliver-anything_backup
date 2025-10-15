package com.deliveranything.domain.order.event.sse.customer;

import com.deliveranything.domain.order.entity.Order;
import com.deliveranything.domain.order.enums.OrderStatus;

public record OrderStatusChangedForCustomerEvent(
    Long orderId,
    Long customerId,
    OrderStatus orderStatus
) {

  public static OrderStatusChangedForCustomerEvent fromOrder(Order order) {
    return new OrderStatusChangedForCustomerEvent(
        order.getId(),
        order.getCustomer().getId(),
        order.getStatus()
    );
  }
}
