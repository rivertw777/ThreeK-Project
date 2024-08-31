package com.ThreeK_Project.api_server.domain.payment.controller;

import com.ThreeK_Project.api_server.domain.payment.dto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentUpdateDto;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PutMapping("/{paymentId}")
    public ResponseEntity<SuccessResponse> updatePayment(@PathVariable("paymentId") UUID paymentId, @RequestBody PaymentUpdateDto requestDto) {
        paymentService.updatePayment(paymentId, requestDto);
        return ResponseEntity.ok(new SuccessResponse("결제 정보 수정 성공"));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable("paymentId") UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDto>> searchUserPayments(@ModelAttribute PaymentSearchDto searchDto) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(paymentService.searchUserPayments(userDetails.getUser().getUsername(), searchDto));
    }
}
