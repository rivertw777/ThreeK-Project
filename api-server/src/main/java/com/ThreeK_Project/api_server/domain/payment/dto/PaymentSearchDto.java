package com.ThreeK_Project.api_server.domain.payment.dto;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentSortType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentSearchDto {
    private Integer page = 0;
    private Integer size = 10;
    private PaymentSortType sortBy = PaymentSortType.CREATED_AT;
    private Boolean ascending = false;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String restaurantName;
    private String username;
}
