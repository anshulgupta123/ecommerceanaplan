package com.example.paymentservicesaga.event;

import com.example.commonservicesaga.event.PaymentCancelledEvent;
import com.example.commonservicesaga.event.PaymentProcessedEvent;
import com.example.paymentservicesaga.aggregate.repository.PaymentRepository;
import com.example.paymentservicesaga.data.Payment;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class PaymentsEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentsEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment
                = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .modifiedOn(new Timestamp(System.currentTimeMillis()))
                .paymentMode(event.getPaymentMode())
                .payableAmount(event.getPayableAmount())
                .build();
        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment
                = paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getPaymentStatus());
        paymentRepository.save(payment);
    }
}