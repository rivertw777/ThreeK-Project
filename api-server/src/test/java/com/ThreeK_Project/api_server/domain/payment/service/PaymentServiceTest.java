package com.ThreeK_Project.api_server.domain.payment.service;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.repository.PaymentRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 정보 생성 성공 테스트?")
    public void createPaymentTest() {
        Order order = new Order();
        PaymentRequestDto requestDto = new PaymentRequestDto(
                PaymentMethod.CARD, new BigDecimal(10000));

        paymentService.createPayment(order, requestDto);
    }

    @Test
    @DisplayName("결제 정보 조회 성공 테스트")
    public void getPaymentTest() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.createPayment(
                PaymentMethod.CARD, PaymentStatus.SUCCESS, new BigDecimal(10000), new Order());

        doReturn(Optional.of(payment))
                .when(paymentRepository)
                .findById(paymentId);

        PaymentResponseDto responseDto = paymentService.getPayment(paymentId);
        assertEquals(responseDto.getPaymentMethod(), PaymentMethod.CARD);
        assertEquals(responseDto.getPaymentStatus(), PaymentStatus.SUCCESS);
        assertEquals(responseDto.getAmount(), new BigDecimal(10000));
    }

    @Test
    @DisplayName("결제 정보 조회 실패 테스트 - 결제 정보 없음")
    public void getPaymentTest2() {
        UUID paymentId = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(paymentRepository)
                .findById(paymentId);

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> paymentService.getPayment(paymentId));
        assertThat(e.getMessage()).isEqualTo("Payment not found");
    }

}