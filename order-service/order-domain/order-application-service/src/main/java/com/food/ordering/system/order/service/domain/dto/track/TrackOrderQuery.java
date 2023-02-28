package com.food.ordering.system.order.service.domain.dto.track;

import javax.validation.constraints.NotNull;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderQuery {

    @NotNull
    private final UUID orderTrackingId;
}
