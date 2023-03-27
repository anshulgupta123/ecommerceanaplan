package com.example.orderservice.controller;


import com.example.orderservice.modal.CategoryDto;
import com.example.orderservice.service.CategoryService;
import com.example.orderservice.utility.UrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping(UrlConstants.ADD_CATEGORY)
    public ResponseEntity<Object> addCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Request for addCategory of CategoryController :{}", categoryDto);
        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.OK);
    }

    @PutMapping(UrlConstants.UPDATE_CATEGORY)
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Request for updateCategory of CategoryController :{}", categoryDto);
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_CATEGORY_BY_ID)
    public ResponseEntity<Object> getCategoryBYId(@RequestParam Long categoryId) {
        log.info("Request for getCategoryBYId of CategoryController :{}", categoryId);
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_ALL_CATEGORY)
    public ResponseEntity<Object> getAllCategory() {
        log.info("Request for getAllCategory of CategoryController");
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.DELETE_CATEGORY)
    public ResponseEntity<Object> deleteCategory(@RequestParam  Long categoryId) {
        log.info("Request for deleteCategory of CategoryController");
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
