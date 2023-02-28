package com.food.ordering.system.order.service.domain.port.output.repository;

import java.util.Optional;

import com.food.ordering.system.order.service.domain.entity.Restaurant;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInfo(Restaurant restaurant);
}
