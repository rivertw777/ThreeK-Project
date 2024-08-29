package com.ThreeK_Project.api_server.domain.payment.service;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.repository.PaymentRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPayment(Order order, PaymentRequestDto requestDto) {
        Payment payment = Payment.createPayment(
                requestDto.getPaymentMethod(), PaymentStatus.SUCCESS,
                requestDto.getPaymentAmount(), order
        );
        paymentRepository.save(payment);
    }

    public PaymentResponseDto getPayment(UUID paymentId) {
        return new PaymentResponseDto(findPaymentById(paymentId));
    }

    public void deletePayment(UUID paymentId, User user) {
        Payment payment = findPaymentById(paymentId);
        payment.deleteBy(user);
        paymentRepository.delete(payment);
    }

    public Payment findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApplicationException("Payment not found"));
    }
}
