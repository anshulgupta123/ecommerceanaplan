package com.example.orderservice.modal;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CartResponseInnerDto implements Serializable {

    private Long cartDetailsId;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Double productPrice;
    private Long createdOn;
    private Long modifiedOn;
}
