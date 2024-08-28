package com.ThreeK_Project.api_server.domain.payment.service;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
