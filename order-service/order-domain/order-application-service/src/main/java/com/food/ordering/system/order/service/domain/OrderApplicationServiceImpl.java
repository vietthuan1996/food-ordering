package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.port.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {
    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final TrackOrderQueryHandler trackOrderQueryHandler;

    @Autowired
    public OrderApplicationServiceImpl(CreateOrderCommandHandler createOrderCommandHandler, TrackOrderQueryHandler trackOrderQueryHandler) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.trackOrderQueryHandler = trackOrderQueryHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return createOrderCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return trackOrderQueryHandler.trackOrder(trackOrderQuery);
    }
}
