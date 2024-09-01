package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderSortType;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
public class OrderSearchDTO {
    @Min(value = 0,  message = "Page is less than 0")
    private Integer page = 0;
    @Range(min = 1, max = 50, message = "Size is less than 0 or more than 50")
    private Integer size = 10;
    private OrderSortType sortBy = OrderSortType.CREATED_AT;
    private Boolean ascending = false;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String restaurantName;
    private String username;
}
