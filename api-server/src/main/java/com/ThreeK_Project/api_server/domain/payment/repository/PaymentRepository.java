package com.ThreeK_Project.api_server.domain.payment.repository;

import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, PaymentRepositoryCustom {
}
