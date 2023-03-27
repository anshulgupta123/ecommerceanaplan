package com.example.orderservice.service;


import com.example.orderservice.modal.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    public Object addCategory(CategoryDto categoryDto);

    public Object updateCategory(CategoryDto categoryDto);

    public Object deleteCategory(Long categoryId);

    public Object getCategoryById(Long categoryId);

    public Object getAllCategory();


}
