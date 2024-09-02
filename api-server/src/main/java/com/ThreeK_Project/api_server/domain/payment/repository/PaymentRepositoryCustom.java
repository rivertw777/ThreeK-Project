package com.ThreeK_Project.api_server.domain.payment.repository;

import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepositoryCustom {

    public Page<Payment> searchPayments(Pageable pageable, PaymentSearchDto searchDto);

}
