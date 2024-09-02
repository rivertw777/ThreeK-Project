package com.ThreeK_Project.api_server.domain.payment.service;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.dto.ResponseDto.PaymentResponseDto;
import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentUpdateDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.repository.PaymentRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // 결제 정보 생성
    @Transactional
    public void createPayment(Order order, PaymentRequestDto requestDto) {
        Payment payment = Payment.createPayment(
                requestDto.getPaymentMethod(), PaymentStatus.SUCCESS,
                requestDto.getPaymentAmount(), order
        );
        paymentRepository.save(payment);
    }

    // 결제 정보 수정
    @Transactional
    public void updatePayment(UUID paymentId, PaymentUpdateDto requestDto) {
        Payment payment = findPaymentById(paymentId);
        payment.updatePayment(requestDto);
        paymentRepository.save(payment);
    }

    // 결제 단일 조회
    @Transactional(readOnly = true)
    public PaymentResponseDto getPayment(UUID paymentId) {
        return new PaymentResponseDto(findPaymentById(paymentId));
    }

    // 사용자 결제 검색
    public Page<PaymentResponseDto> searchUserPayments(String username, PaymentSearchDto searchDto) {
        if(searchDto.getUsername() == null)
            searchDto.setUsername(username);
        else if(!searchDto.getUsername().equals(username))
            throw new ApplicationException("Invalid user");

        return searchPayments(searchDto);
    }

    // 가게 주인 결제 검색
    public Page<PaymentResponseDto> searchRestaurantPayments(String restaurantName, PaymentSearchDto searchDto) {
        if(searchDto.getRestaurantName() == null)
            searchDto.setRestaurantName(restaurantName);
        else if(!searchDto.getRestaurantName().equals(restaurantName))
            throw new ApplicationException("Invalid restaurant");

        return searchPayments(searchDto);
    }

    // 결제 검색
    public Page<PaymentResponseDto> searchPayments(PaymentSearchDto searchDto) {
        Sort sort = searchDto.getAscending() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy().getValue())
                : Sort.by(Sort.Direction.DESC, searchDto.getSortBy().getValue());

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        return paymentRepository.searchPayments(pageable, searchDto).map(PaymentResponseDto::new);
    }

    @Transactional
    public void deletePayment(UUID paymentId, User user) {
        Payment payment = findPaymentById(paymentId);
        payment.deleteBy(user);
        paymentRepository.delete(payment);
    }

    @Transactional
    public void canclePayment(Payment cancledPayment) {
        cancledPayment.changeStatus(PaymentStatus.CANCELED);
        paymentRepository.save(cancledPayment);
    }

    @Transactional
    public Payment findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApplicationException("Payment not found"));
    }
}
