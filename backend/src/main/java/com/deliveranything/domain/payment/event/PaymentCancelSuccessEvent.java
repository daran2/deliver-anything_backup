package com.deliveranything.domain.payment.event;

import com.deliveranything.domain.order.enums.Publisher;

public record PaymentCancelSuccessEvent(String merchantUid, Publisher publisher) {

}
