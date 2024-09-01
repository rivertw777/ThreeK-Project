package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderStatusRequestDto;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/orders")
public class OrderOwnerController {

    private final OrderService orderService;

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "가게 주인 주문 상태 변경")
    public ResponseEntity<SuccessResponse> updateOrderStatus(
            @PathVariable("orderId") UUID orderId,
            @RequestBody @Valid OrderStatusRequestDto requestDto
    ) {
        orderService.updateOrderStatus(orderId, requestDto.getOrderStatus());
        return ResponseEntity.ok(new SuccessResponse("주문 상태 변경 성공"));
    }
}
