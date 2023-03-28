package com.example.orderservice.modal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Data
public class OrderRestModel {
    private String orderId;
    private Double totalAmount;
    private Long userId;
    private String orderStatus;
    private Long createdOn;
    private Long modifiedOn;

}

