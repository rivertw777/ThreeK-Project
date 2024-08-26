package com.ThreeK_Project.api_server.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProduct{
    private UUID productId;
    private Integer quantity;
}
