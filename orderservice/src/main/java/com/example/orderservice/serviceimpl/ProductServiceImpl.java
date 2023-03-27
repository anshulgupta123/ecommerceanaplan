package com.example.orderservice.serviceimpl;


import com.example.orderservice.data.Category;
import com.example.orderservice.data.Product;
import com.example.orderservice.exception.ProductException;
import com.example.orderservice.modal.CategoryDto;
import com.example.orderservice.modal.ProductRequestDto;
import com.example.orderservice.modal.ProductResponseDto;
import com.example.orderservice.modal.Response;
import com.example.orderservice.repository.CategoryRepository;
import com.example.orderservice.repository.ProductRespository;
import com.example.orderservice.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    Environment environment;

    @Autowired
    ProductRespository productRespository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Object addProduct(ProductRequestDto productRequestDto) {
        log.info("Inside addProduct of ProductServiceImpl");
        try {
            validateAddProduct(productRequestDto);
            Product savedProduct = productRespository.save(populateProductFromProductRequestyDto(productRequestDto));
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.PRODUCT_ADDED_SUCCESSFULLY), savedProduct);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof ProductException) {
                errorMessage = ((ProductException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in addProduct of ProductServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new ProductException(errorMessage);
        }
    }

    @Override
    public Object updateProduct(ProductRequestDto productRequestDto) {
        log.info("Inside updateProduct of ProductServiceImpl");
        try {
            Product product = validateUpdateProduct(productRequestDto);
            Product updatedProduct = productRespository.save(populateProductFromProductRequestyDtoForUpdate(productRequestDto, product));
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.PRODUCT_UPDATED_SUCCESSFULLY), updatedProduct);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof ProductException) {
                errorMessage = ((ProductException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in updateProduct of ProductServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new ProductException(errorMessage);
        }
    }

    @Override
    public Object deleteProduct(Long productId) {
        log.info("Inside deleteProduct of ProductServiceImpl");
        try {
            validateDeleteProduct(productId);
            productRespository.deleteById(productId);
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.PRODUCT_DELETED_SUCCESSFULLY));
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof ProductException) {
                errorMessage = ((ProductException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in deleteProduct of ProductServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new ProductException(errorMessage);
        }
    }

    @Override
    public Object getProductById(Long productId) {
        log.info("Inside getProductById of ProductServiceImpl");
        try {
            Product savedProduct = valiadateGetProductById(productId);
            ProductResponseDto productResponseDto = populateProductResponseDtoFromProduct(savedProduct);
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.PRODUCT_FETCHED_SUCCESSFULLY), productResponseDto);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof ProductException) {
                errorMessage = ((ProductException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getProductById of ProductServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new ProductException(errorMessage);
        }
    }

    @Override
    public Object getAllProducts(String serachParam) {
        log.info("Inside getAllProducts of ProductServiceImpl");
        try {
            List<Product> listOfProducts = productRespository.findAllPoductBySearchParam(serachParam);
            log.info("Size of list from repo is :{}", listOfProducts.size());
            List<ProductResponseDto> responseDtoList = new ArrayList<>();
            for (Product product : listOfProducts) {
                responseDtoList.add(populateProductResponseDtoFromProduct(product));
            }
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.PRODUCT_FETCHED_SUCCESSFULLY), responseDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = null;
            if (e instanceof ProductException) {
                errorMessage = ((ProductException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getProductById of ProductServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new ProductException(errorMessage);
        }
    }

    public void validateAddProduct(ProductRequestDto productRequestDto) throws Exception {
        log.info("Inside validateAddProduct of ProductServiceImpl");
        if (productRequestDto.getProductName() == null || productRequestDto.getProductName().isEmpty() || productRequestDto.getProductCount() == null ||
                productRequestDto.getProductCount() < 1 || productRequestDto.getProductPrice() == null || productRequestDto.getProductPrice() < 1.00 ||
                productRequestDto.getCategoryId() == null) {
            throw new ProductException(Constants.INVALID_REQUEST_DATA);
        }
    }

    public Product validateUpdateProduct(ProductRequestDto productRequestDto) throws Exception {
        log.info("Inside validateAddProduct of ProductServiceImpl");
        if (productRequestDto.getProductId() == null) {
            throw new ProductException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        Optional<Product> product = productRespository.findById(productRequestDto.getProductId());
        if (!product.isPresent()) {
            throw new ProductException(environment.getProperty(Constants.PRODUCT_DOES_NOT_EXIST));
        }
        return product.get();
    }


    public Product populateProductFromProductRequestyDto(ProductRequestDto productRequestDto) throws Exception {
        log.info("Inside populateProductFromProductRequestyDtoForUpdate of ProductServiceImpl");
        Product product = new Product();
        product.setProductCount(productRequestDto.getProductCount());
        product.setProductName(productRequestDto.getProductName());
        product.setProductPrice(productRequestDto.getProductPrice());
        Optional<Category> category = categoryRepository.findById(productRequestDto.getCategoryId());
        if (!category.isPresent()) {
            throw new ProductException(environment.getProperty(Constants.CATEGORY_DOES_NOT_EXIST));
        }
        product.setCategory(category.get());
        product.setCreatedOn(getCurrentTimeStamp());
        product.setModifiedOn(getCurrentTimeStamp());
        return product;
    }

    public Product populateProductFromProductRequestyDtoForUpdate(ProductRequestDto productRequestDto, Product product) throws Exception {
        log.info("Inside populateProductFromProductRequestyDtoForUpdate of ProductServiceImpl");
        product.setProductCount(productRequestDto.getProductCount());
        product.setProductName(productRequestDto.getProductName());
        product.setProductPrice(productRequestDto.getProductPrice());
        Optional<Category> category = categoryRepository.findById(productRequestDto.getCategoryId());
        if (!category.isPresent()) {
            throw new ProductException(environment.getProperty(Constants.CATEGORY_DOES_NOT_EXIST));
        }
        product.setCategory(category.get());
        product.setModifiedOn(getCurrentTimeStamp());
        return product;
    }


    public Timestamp getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Product valiadateGetProductById(Long productId) throws Exception {
        log.info("Inside valiadateGetProductById of ProductServiceImpl");
        if (productId == null) {
            log.info("Getting product id as null");
            throw new ProductException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        Optional<Product> product = productRespository.findById(productId);
        if (!product.isPresent()) {
            log.info("Product deoes not exist");
            throw new ProductException(environment.getProperty(Constants.PRODUCT_DOES_NOT_EXIST));
        }
        return product.get();
    }

    public void validateDeleteProduct(Long productId) throws Exception {
        log.info("Inside valiadateDeletProduct of ProductServiceImpl");
        if (productId == null) {
            log.info("Getting product id as null");
            throw new ProductException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        Optional<Product> product = productRespository.findById(productId);
        if (!product.isPresent()) {
            log.info("Product deoes not exist");
            throw new ProductException(environment.getProperty(Constants.PRODUCT_DOES_NOT_EXIST));
        }
    }

    public ProductResponseDto populateProductResponseDtoFromProduct(Product product) throws Exception {
        log.info("Inside populateProductResponseDtoFromProduct of ProductServiceImpl");
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setProductId(product.getProductId());
        productResponseDto.setProductName(product.getProductName());
        productResponseDto.setProductCount(product.getProductCount());
        productResponseDto.setProductPrice(product.getProductPrice());
        Category category = product.getCategory();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        productResponseDto.setCategory(categoryDto);
        productResponseDto.setCreatedOn(product.getCreatedOn() != null ? product.getCreatedOn().getTime() : null);
        productResponseDto.setModifiedOn(product.getModifiedOn() != null ? product.getModifiedOn().getTime() : null);
        return productResponseDto;
    }
}
