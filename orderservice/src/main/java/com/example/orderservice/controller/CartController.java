package com.example.orderservice.controller;

import com.example.orderservice.modal.CartOuterRequestDto;
import com.example.orderservice.service.CartService;
import com.example.orderservice.utility.UrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CartController {


    @Autowired
    CartService cartService;

    @PostMapping(value = UrlConstants.ADD_ITEMS_TO_CART)
    public ResponseEntity<Object> addCartItems(@RequestBody CartOuterRequestDto cartOuterRequestDto) {
        log.info("Request for addCartItems of CartController");
        return new ResponseEntity<>(cartService.addItemsToCart(cartOuterRequestDto), HttpStatus.OK);
    }

    @GetMapping(value = UrlConstants.GET_CART_BY_ID)
    public ResponseEntity<Object> getCartById(@RequestParam Long cartId) {
        log.info("Request for getCartById of CartController");
        return new ResponseEntity<>(cartService.getCartById(cartId), HttpStatus.OK);
    }

    @GetMapping(value = UrlConstants.GET_CART_BY_USER_ID)
    public ResponseEntity<Object> getCartByUserId(@RequestParam Long userId) {
        log.info("Request for getCartByUserId of CartController");
        return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
    }
}
