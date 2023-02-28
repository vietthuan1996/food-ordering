package com.food.ordering.system.order.service.domain.entity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.order.service.domain.exception.DomainException;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueObjects.OrderItemId;
import com.food.ordering.system.order.service.domain.valueObjects.StreetAddress;
import com.food.ordering.system.order.service.domain.valueObjects.TrackingId;
import com.food.ordering.system.valueObjects.CustomerId;
import com.food.ordering.system.valueObjects.Money;
import com.food.ordering.system.valueObjects.OrderId;
import com.food.ordering.system.valueObjects.OrderStatus;
import com.food.ordering.system.valueObjects.RestaurantId;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.streetAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateInitialTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (!orderStatus.equals(OrderStatus.PENDING)) {
            throw new OrderDomainException("The order state is not correct with the pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (!orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("The order state is not correct with the approve operation");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initialCancel(List<String> failureMessages) {
        if (!orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("The order state is not correct with the initialCancel operation");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus.equals(OrderStatus.PENDING) || orderStatus.equals(OrderStatus.CANCELLING))) {
            throw new OrderDomainException("The order state is not correct with the cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }


    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(item -> {
            validateItemPrice(item);
            return item.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount() + " is not equal the total items price: " + orderItemsTotal);
        }
    }

    private void validateItemPrice(OrderItem item) {
        if (!item.isPriceValid()) {
            throw new OrderDomainException("Price of item is not valid");
        }
    }

    private void validateInitialTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price should greater than zero");
        }
    }

    private void validateInitialOrder() throws OrderDomainException {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization");
        }
    }

    private void initializeItems() {
        long itemId = 1;
        for (OrderItem orderItem: items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId id;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder streetAddress(StreetAddress val) {
            streetAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
