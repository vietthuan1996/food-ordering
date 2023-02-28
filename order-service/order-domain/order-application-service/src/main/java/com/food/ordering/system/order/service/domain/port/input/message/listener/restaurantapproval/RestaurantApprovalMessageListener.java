package com.food.ordering.system.order.service.domain.port.input.message.listener.restaurantapproval;

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {
    void orderApproved(RestaurantApprovalResponse response);

    void orderRejected(RestaurantApprovalResponse response);
}
