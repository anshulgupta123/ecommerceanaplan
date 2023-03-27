package com.example.orderservice.service;

import com.example.orderservice.modal.CategoryDto;
import com.example.orderservice.modal.ProductRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    public Object addProduct(ProductRequestDto productRequestDto);

    public Object updateProduct(ProductRequestDto productRequestDto);

    public Object deleteProduct(Long productId);

    public Object getProductById(Long productId);

    public Object getAllProducts(String serachParam);
}
