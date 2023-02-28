package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderMapper;
import com.food.ordering.system.order.service.domain.port.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderCommandHandler {
    private final OrderCreateHelper orderCreateHelper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;
    private final OrderMapper orderMapper;

    @Autowired
    public CreateOrderCommandHandler(
        OrderCreateHelper orderCreateHelper,
        OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher,
        OrderMapper orderMapper) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
        this.orderMapper = orderMapper;
    }
    public CreateOrderResponse createOrder(CreateOrderCommand orderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(orderCommand);
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }
}
