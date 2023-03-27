package com.example.orderservice.modal;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {
    private Long categoryId;
    private String categoryName;
}
