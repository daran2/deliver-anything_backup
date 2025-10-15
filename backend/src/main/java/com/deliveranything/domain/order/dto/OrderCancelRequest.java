package com.deliveranything.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderCancelRequest(@NotNull @NotBlank @Size(max = 200) String cancelReason) {

}
