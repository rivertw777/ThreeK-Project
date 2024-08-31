package com.ThreeK_Project.api_server.domain.payment.controller;

import com.ThreeK_Project.api_server.domain.payment.dto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class PaymentAdminController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Page<PaymentResponseDto>> searchPayments(@ModelAttribute PaymentSearchDto searchDto) {
        return ResponseEntity.ok(paymentService.searchPayments(searchDto));
    }
}
