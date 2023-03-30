package com.example.commonservicesaga.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedEvent {
    private String paymentId;
    private String orderId;
    private Double payableAmount;
    private String paymentMode;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private String paymentStatus;
}
