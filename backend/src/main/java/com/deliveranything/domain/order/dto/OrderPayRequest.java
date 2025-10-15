package com.deliveranything.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderPayRequest(@NotNull @Size(max = 200) String paymentKey) {

}
