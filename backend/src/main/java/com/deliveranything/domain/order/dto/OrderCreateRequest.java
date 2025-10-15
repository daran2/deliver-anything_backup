package com.deliveranything.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderCreateRequest(
    @NotNull @Positive Long storeId,
    @NotNull @NotEmpty @Valid List<OrderItemRequest> orderItemRequests,
    @NotBlank @Size(max = 100) String address,
    @NotNull Double lat,
    @NotNull Double lng,
    @Size(max = 30) String riderNote,
    @Size(max = 30) String storeNote,
    @NotNull @Positive Long totalPrice,
    @NotNull @Positive Long storePrice,
    @NotNull @Positive Long deliveryPrice
) {

}
