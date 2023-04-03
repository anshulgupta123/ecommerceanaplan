package com.example.paymentservicesaga.aggregate.repository;

import com.example.paymentservicesaga.data.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
