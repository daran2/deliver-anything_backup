package com.deliveranything.domain.order.service;

import com.deliveranything.domain.order.dto.OrderResponse;
import com.deliveranything.domain.order.entity.Order;
import com.deliveranything.domain.order.repository.OrderRepository;
import com.deliveranything.global.exception.CustomException;
import com.deliveranything.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeliveryOrderService {

  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public OrderResponse getOrderByDeliveryId(Long deliveryId) {
    return OrderResponse.from(orderRepository.findOrderWithStoreByDeliveryId(deliveryId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND)));
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getRiderDeliveryOrders(Long riderProfileId) {
    return orderRepository.findOrdersWithStoreByRiderProfile(riderProfileId).stream()
        .map(OrderResponse::from)
        .toList();
  }

  public Order getOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
  }

  public Long getCustomerIdByOrderId(Long orderId) {
    return getOrderById(orderId).getCustomer().getId();
  }

  public Long getSellerIdByOrderId(Long orderId) {
    return getOrderById(orderId).getStore().getSellerProfileId();
  }
}
