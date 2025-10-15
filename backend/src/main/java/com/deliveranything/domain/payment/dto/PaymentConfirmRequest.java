package com.deliveranything.domain.payment.dto;

public record PaymentConfirmRequest(String paymentKey, String merchantUid, Long amount) {

}
