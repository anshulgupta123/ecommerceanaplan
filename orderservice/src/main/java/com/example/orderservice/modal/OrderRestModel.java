package com.example.orderservice.modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRestModel {
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;

    public OrderRestModel() {
    }

    public OrderRestModel(String productId, String userId, String addressId, Integer quantity) {
        this.productId = productId;
        this.userId = userId;
        this.addressId = addressId;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "OrderRestModel{" +
                "productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                ", addressId='" + addressId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

