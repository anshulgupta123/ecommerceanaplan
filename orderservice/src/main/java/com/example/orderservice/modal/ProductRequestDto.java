package com.example.orderservice.modal;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductRequestDto implements Serializable {
    private Long productId;
    private String productName;
    private Double productPrice;
    private Long categoryId;
    private Long createdOn;
    private Long modifiedOn;
    private Integer productCount;

}
