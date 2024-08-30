package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDTO {
    private String sortBy = "createdAt";
    private Boolean ascending = false;
    private LocalDate startDate;
    private LocalDate endDate;
    private String restaurantName;
    private String userName;
}
