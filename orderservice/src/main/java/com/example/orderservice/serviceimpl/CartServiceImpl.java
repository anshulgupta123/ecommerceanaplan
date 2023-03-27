package com.example.orderservice.serviceimpl;

import com.example.orderservice.data.Cart;
import com.example.orderservice.data.CartDetails;
import com.example.orderservice.exception.CartException;
import com.example.orderservice.modal.*;
import com.example.orderservice.repository.CartDetailsRepository;
import com.example.orderservice.repository.CartRepository;
import com.example.orderservice.service.CartService;
import com.example.orderservice.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    Environment environment;

    @Autowired
    CartRepository cartRepository;


    @Autowired
    CartDetailsRepository cartDetailsRepository;

    @Override
    public Object addItemsToCart(CartOuterRequestDto cartOuterRequestDto) {
        log.info("Inside addItemsToCart of CartServiceImpl");
        try {
            Double totalAmount = 0.0;
            List<CartDetails> cartDetailsList = new ArrayList<>();
            Cart cart = new Cart();
            cart.setUserId(cartOuterRequestDto.getUserId());
            Cart savedCart = cartRepository.save(cart);
            for (CartRequestInnerDto cartRequestInnerDto : cartOuterRequestDto.getListOfCartItems()) {
                CartDetails cartDetails = new CartDetails();
                cartDetails.setCartId(savedCart);
                cartDetails.setQuantity(cartRequestInnerDto.getQuantity());
                cartDetails.setProductId(cartRequestInnerDto.getProductId());
                cartDetails.setCreatedOn(getCurrentTimestamp());
                cartDetails.setModifiedOn(getCurrentTimestamp());
                cartDetails.setProductPrice(cartRequestInnerDto.getProductPrice());
                totalAmount = totalAmount + (cartRequestInnerDto.getProductPrice() * cartRequestInnerDto.getQuantity());
                cartDetailsList.add(cartDetails);
            }
            cartDetailsRepository.saveAll(cartDetailsList);
            savedCart.setTotalAmount(totalAmount);
            savedCart.setCartDetailsList(cartDetailsList);
            Cart finalSavedCart = cartRepository.save(savedCart);
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CART_ITEMS_ADDED_SUCCESSFULLY));
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CartException) {
                errorMessage = ((CartException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in addItemsToCart of CartServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CartException(errorMessage);
        }

    }

    @Override
    public Object getCartById(Long cartId) {
        log.info("Inside getCartById of CartServiceImpl");
        try {
            Optional<Cart> cart = cartRepository.findById(cartId);
            if (!cart.isPresent()) {
                throw new CartException(environment.getProperty(Constants.NO_DATA_FOUND));
            }
            CartOuterResponseDto cartOuterResonseDto = new CartOuterResponseDto();
            List<CartDetails> respFromRepo = cart.get().getCartDetailsList();
            cartOuterResonseDto.setListOfCartItems(populateListResponseCartDetails(respFromRepo));
            cartOuterResonseDto.setUserId(cart.get().getUserId());
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CART_ITEMS_FETCHED_SUCCESSFULLY),cartOuterResonseDto);

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CartException) {
                errorMessage = ((CartException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getCartById of CartServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CartException(errorMessage);
        }
    }

    @Override
    public Object getCartByUserId(Long userId) {
        log.info("Inside getCartByUserId of CartServiceImpl");
        try {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            if (!cart.isPresent()) {
                throw new CartException(environment.getProperty(Constants.NO_DATA_FOUND));
            }
            CartOuterResponseDto cartOuterResonseDto = new CartOuterResponseDto();
            List<CartDetails> respFromRepo = cart.get().getCartDetailsList();
            cartOuterResonseDto.setListOfCartItems(populateListResponseCartDetails(respFromRepo));
            cartOuterResonseDto.setUserId(cart.get().getUserId());
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CART_ITEMS_FETCHED_SUCCESSFULLY),cartOuterResonseDto);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CartException) {
                errorMessage = ((CartException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getCartByUserId of CartServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CartException(errorMessage);
        }
    }

    public List<CartResponseInnerDto> populateListResponseCartDetails(List<CartDetails> cartDetails) {
        log.info("Inside populateListResponseCartDetails of CartServiceImpl");
        List<CartResponseInnerDto> cartResponseInnerDtoList = new ArrayList<>();
        for (CartDetails cartDetails1 : cartDetails) {
            CartResponseInnerDto cartResponseInnerDto = new CartResponseInnerDto();
            cartResponseInnerDto.setCartDetailsId(cartDetails1.getCartDetailsId());
            cartResponseInnerDto.setCartId(cartDetails1.getCartId().getCartId());
            cartResponseInnerDto.setQuantity(cartDetails1.getQuantity());
            cartResponseInnerDto.setProductPrice(cartDetails1.getProductPrice());
            cartResponseInnerDto.setModifiedOn(cartDetails1.getModifiedOn() != null ? cartDetails1.getModifiedOn().getTime() : null);
            cartResponseInnerDto.setCreatedOn(cartDetails1.getCreatedOn() != null ? cartDetails1.getCreatedOn().getTime() : null);
            cartResponseInnerDto.setProductId(cartDetails1.getProductId());
            cartResponseInnerDtoList.add(cartResponseInnerDto);
        }
        return cartResponseInnerDtoList;
    }

    public Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

