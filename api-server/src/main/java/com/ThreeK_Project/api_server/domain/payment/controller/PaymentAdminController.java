package com.ThreeK_Project.api_server.domain.payment.controller;

import com.ThreeK_Project.api_server.domain.payment.dto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentSearchDto;
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
@RequestMapping("/api/admin/payments")
public class PaymentAdminController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDto>> searchPayments(@ModelAttribute PaymentSearchDto searchDto) {
        return ResponseEntity.ok(paymentService.searchPayments(searchDto));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<SuccessResponse> deletePayment(@PathVariable("paymentId") UUID paymentId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        paymentService.deletePayment(paymentId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse("결제 정보 삭제 성공"));
    }
}
