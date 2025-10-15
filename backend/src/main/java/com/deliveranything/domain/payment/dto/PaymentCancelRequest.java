package com.deliveranything.domain.payment.dto;

public record PaymentCancelRequest(String paymentKey, String cancelReason) {

}
