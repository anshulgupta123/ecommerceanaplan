package com.example.orderservice.modal;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CartOuterResponseDto implements Serializable {

    private List<CartResponseInnerDto> listOfCartItems;
    private Long userId;
}
