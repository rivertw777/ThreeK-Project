package com.ThreeK_Project.api_server.domain.payment.controller;

import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentUpdateDto;
import com.ThreeK_Project.api_server.domain.payment.dto.ResponseDto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    @Operation(summary = "관리자 결제 정보 검색")
    public ResponseEntity<Page<PaymentResponseDto>> searchPayments(@ModelAttribute @Valid PaymentSearchDto searchDto) {
        return ResponseEntity.ok(paymentService.searchPayments(searchDto));
    }

    @PutMapping("/{paymentId}")
    @Operation(summary = "사용자 결제 정보 수정")
    public ResponseEntity<SuccessResponse> updatePayment(
            @PathVariable("paymentId") UUID paymentId,
            @RequestBody @Valid PaymentUpdateDto requestDto
    ) {
        paymentService.updatePayment(paymentId, requestDto);
        return ResponseEntity.ok(new SuccessResponse("결제 정보 수정 성공"));
    }

    @DeleteMapping("/{paymentId}")
    @Operation(summary = "관리자 결제 정보 삭제")
    public ResponseEntity<SuccessResponse> deletePayment(@PathVariable("paymentId") UUID paymentId) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        paymentService.deletePayment(paymentId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse("결제 정보 삭제 성공"));
    }
}
