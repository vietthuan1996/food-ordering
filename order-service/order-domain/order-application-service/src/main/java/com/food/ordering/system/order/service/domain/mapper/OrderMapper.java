package com.food.ordering.system.order.service.domain.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueObjects.StreetAddress;
import com.food.ordering.system.valueObjects.CustomerId;
import com.food.ordering.system.valueObjects.Money;
import com.food.ordering.system.valueObjects.ProductId;
import com.food.ordering.system.valueObjects.RestaurantId;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand orderCommand) {
        return Restaurant.Builder.builder()
            .id(new RestaurantId(orderCommand.getRestaurantId()))
            .products(
                orderCommand.getOrderItems().stream()
                            .map(item -> new Product(new ProductId(item.getProductId())))
                            .collect(Collectors.toList()))
            .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand orderCommand) {
        return Order.Builder.builder()
            .customerId(new CustomerId(orderCommand.getCustomerId()))
            .restaurantId(new RestaurantId(orderCommand.getRestaurantId()))
            .streetAddress(orderAddressToStreetAddress(orderCommand.getOrderAddress()))
            .price(new Money(orderCommand.getPrice()))
            .items(orderItemsToOrderItemEntities(orderCommand.getOrderItems()))
            .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order result) {
        return CreateOrderResponse.builder()
            .orderTrackingId(result.getTrackingId().getValue())
            .orderStatus(result.getOrderStatus())
            .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                         .map(orderItem -> OrderItem.Builder.builder()
                             .product(new Product(new ProductId(orderItem.getProductId())))
                             .price(new Money(orderItem.getPrice()))
                             .quantity(orderItem.getQuantity())
                             .subTotal(new Money(orderItem.getSubTotal()))
                             .build())
                         .collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(UUID.randomUUID(), orderAddress.getStreet(), orderAddress.getPostalCode(), orderAddress.getCity());
    }
}
