package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestaurantRepository extends CrudRepository<Restaurant, UUID> {
}
