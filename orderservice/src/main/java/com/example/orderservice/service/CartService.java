package com.example.orderservice.service;

import com.example.orderservice.modal.CartOuterRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    Object addItemsToCart(CartOuterRequestDto cartOuterRequestDto);

    Object getCartById(Long cartId);

    Object getCartByUserId(Long userId);

}

