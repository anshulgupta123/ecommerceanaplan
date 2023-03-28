package com.example.orderservice.events;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderCreatedEvent {

    private String orderId;
    private Double totalAmount;
    private Long userId;
    private String orderStatus;
    private Long createdOn;
    private Long modifiedOn;
}
