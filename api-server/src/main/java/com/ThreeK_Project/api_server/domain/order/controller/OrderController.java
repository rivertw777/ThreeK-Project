package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderSearchDTO;
import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderStatusRequestDto;
import com.ThreeK_Project.api_server.domain.order.dto.ResponseDto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @Operation(summary = "사용자 주문 취소")
    public ResponseEntity<SuccessResponse> cancelOrder(@PathVariable("orderId") UUID orderId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.cancelOrder(orderId, userDetails.getUser().getUsername());
        return ResponseEntity.ok(new SuccessResponse("주문 취소 성공"));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "사용자 주문 조회")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("orderId") UUID orderId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(orderService.getOrder(userDetails.getUser(), orderId));
    }

    @GetMapping
    @Operation(summary = "사용자 주문 검색")
    public ResponseEntity<Page<OrderResponseDto>> searchUserOrders(@ModelAttribute OrderSearchDTO searchDTO) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(orderService.searchUserOrders(userDetails.getUser().getUsername(), searchDTO));
    }

    @PostMapping("{orderId}/payments")
    @Operation(summary = "사용자 결제 정보 생성")
    public ResponseEntity<SuccessResponse> createPayment(
            @PathVariable("orderId") UUID orderId, @RequestBody PaymentRequestDto requestDto
    ) {
        Order order = orderService.findOrderById(orderId);
        paymentService.createPayment(order, requestDto);
        return ResponseEntity.ok(new SuccessResponse("결제 정보 생성 성공"));
    }

}
