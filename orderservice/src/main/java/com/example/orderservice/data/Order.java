package com.example.orderservice.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;
    private Double totalAmount;
    private Long userId;
    private String orderStatus;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
}
