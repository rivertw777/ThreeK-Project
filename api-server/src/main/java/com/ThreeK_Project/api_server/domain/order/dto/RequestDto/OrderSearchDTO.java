package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderSortType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchDTO {
    private Integer page = 0;
    private Integer size = 10;
    private OrderSortType sortBy = OrderSortType.CREATED_AT;
    private Boolean ascending = false;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String restaurantName;
    private String username;
}
