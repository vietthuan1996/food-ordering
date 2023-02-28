package com.food.ordering.system.order.service.domain.dto.create;

import javax.validation.constraints.NotNull;

import java.util.UUID;

import com.food.ordering.system.valueObjects.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    @NotNull
    private final UUID orderTrackingId;

    @NotNull
    private final OrderStatus orderStatus;

    @NotNull
    private final String message;
}
