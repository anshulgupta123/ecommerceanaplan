package com.example.orderservice.modal;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CartOuterRequestDto implements Serializable {

    private List<CartRequestInnerDto> listOfCartItems;
    private Long userId;
}
