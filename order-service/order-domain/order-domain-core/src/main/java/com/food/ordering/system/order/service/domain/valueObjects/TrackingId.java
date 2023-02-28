package com.food.ordering.system.order.service.domain.valueObjects;

import java.util.UUID;

import com.food.ordering.system.valueObjects.BaseId;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
