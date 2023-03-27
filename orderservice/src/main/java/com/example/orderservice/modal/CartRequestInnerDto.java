package com.example.orderservice.modal;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CartRequestInnerDto implements Serializable {

    private Long cartDetailsId;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Double productPrice;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
}
