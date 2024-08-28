package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.domain.order.dto.OrderRequestDto;
import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 총 금액을 입력받아서 저장하는 게 맞을지 product에서 각각 값 가져와서 더해서 만드는게 맞을지
    // 지금은 명세서대로 입력받은 값으로 저장하게 되어있음
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto requestDto) {
        orderService.createOrder(requestDto);
        return ResponseEntity.ok("\"message\": \"주문 생성 성공\"");
    }

    // 반환 시에 지금은 product에 대해 productId, quantity만 반환하는데 다른 것도 반환하면 어떨지?
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse> deleteOrder(@PathVariable UUID orderId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse("주문 삭제 성공"));
    }

}
