package com.food.ordering.system.order.service.domain.dto.track;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import com.food.ordering.system.valueObjects.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {

    @NotNull
    private final UUID orderTrackingId;

    @NotNull
    private final OrderStatus orderStatus;
    private List<String> failureMessages;
}
