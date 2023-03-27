package com.example.orderservice.controller;

import com.example.orderservice.modal.ProductRequestDto;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.utility.UrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ProductController {


    @Autowired
    ProductService productService;

    @PostMapping(UrlConstants.ADD_PRODUCT)
    public ResponseEntity<Object> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        log.info("Request for addProduct of ProductController :{}", productRequestDto);
        return new ResponseEntity<>(productService.addProduct(productRequestDto), HttpStatus.OK);
    }

    @PutMapping(UrlConstants.UPDATE_PRODUCT)
    public ResponseEntity<Object> updateProduct(@RequestBody ProductRequestDto productRequestDto) {
        log.info("Request for updateProduct of ProductController :{}", productRequestDto);
        return new ResponseEntity<>(productService.updateProduct(productRequestDto), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_PRODUCT_BY_ID)
    public ResponseEntity<Object> getProductById(@RequestParam Long productId) {
        log.info("Request for getProductById of ProductController :{}", productId);
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_ALL_PRODUCT_WITH_SEARCH_PARAM)
    public ResponseEntity<Object> getAllProductsBySearchParam(@RequestParam(defaultValue = "") String searchParam) {
        log.info("Request for getAllProductsBySearchParam of ProductController :{}", searchParam);
        return new ResponseEntity<>(productService.getAllProducts(searchParam), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.DELETE_PRODUCT)
    public ResponseEntity<Object> deleteProduct(@RequestParam Long productId) {
        log.info("Request for deleteProduct of ProductController :{}", productId);
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

}
