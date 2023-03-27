package com.example.orderservice.modal;

import com.example.orderservice.data.Category;
import lombok.Data;

import java.io.Serializable;


@Data
public class ProductResponseDto implements Serializable {

    private Long productId;
    private String productName;
    private Double productPrice;
    private CategoryDto category;
    private Long createdOn;
    private Long modifiedOn;
    private Integer productCount;
}
