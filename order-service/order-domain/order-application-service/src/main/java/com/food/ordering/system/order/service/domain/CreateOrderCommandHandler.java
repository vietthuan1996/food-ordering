package com.food.ordering.system.order.service.domain;

import java.util.Optional;
import java.util.UUID;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderMapper;
import com.food.ordering.system.order.service.domain.port.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CreateOrderCommandHandler {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public CreateOrderCommandHandler(OrderDomainService orderDomainService, OrderRepository orderRepository,
        CustomerRepository customerRepository, RestaurantRepository restaurantRepository, OrderMapper orderMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand orderCommand) {
        checkCustomer(orderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(orderCommand);
        Order order = orderMapper.createOrderCommandToOrder(orderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order result = saveOrder(order);
        log.info("Order created with id: {}", result.getId().getValue());
        return orderMapper.orderToCreateOrderResponse(result);
    }

    private Order saveOrder(Order order) {
        Order result = orderRepository.save(order);
        if (result == null) {
            log.warn("Can not save Order!");
            throw new OrderDomainException("Can not save Order!");
        }
        log.info("Order saved with id: {}", order.getId().getValue());
        return result;
    }

    private Restaurant checkRestaurant(CreateOrderCommand orderCommand) {
        Restaurant restaurant = orderMapper.createOrderCommandToRestaurant(orderCommand);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInfo(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.warn("Restaurant with id: {} does not exist", restaurant.getId().getValue());
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() + " does not exist");
        }
        return restaurantOptional.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer((customerId));

        if (customer.isEmpty()) {
            log.warn("Customer id: {} does not exist!", customer);
            throw new OrderDomainException("Customer id:" + customer + "does not exist!");
        }
    }
}
