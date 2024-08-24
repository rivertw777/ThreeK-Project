package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {
}
