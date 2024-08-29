package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.OrderStatusRequestDto;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<SuccessResponse> cancelOrder(@PathVariable("orderId") UUID orderId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.cancelOrder(orderId, userDetails.getUser().getUsername());
        return ResponseEntity.ok(new SuccessResponse("주문 취소 성공"));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<SuccessResponse> updateOrderStatus(
            @PathVariable("orderId") UUID orderId,
            @RequestBody OrderStatusRequestDto requestDto
    ) {
        orderService.updateOrderStatus(orderId, requestDto.getOrderStatus());
        return ResponseEntity.ok(new SuccessResponse("주문 상태 변경 성공"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("orderId") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse> deleteOrder(@PathVariable("orderId") UUID orderId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse("주문 삭제 성공"));
    }

    @PostMapping("{orderId}/payments")
    public ResponseEntity<SuccessResponse> createPayment(
            @PathVariable("orderId") UUID orderId, @RequestBody PaymentRequestDto requestDto
    ) {
        Order order = orderService.findOrderById(orderId);
        paymentService.createPayment(order, requestDto);
        return ResponseEntity.ok(new SuccessResponse("결제 정보 생성 성공"));
    }

}
