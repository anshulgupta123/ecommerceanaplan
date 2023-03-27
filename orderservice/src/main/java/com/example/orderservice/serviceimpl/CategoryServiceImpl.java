package com.example.orderservice.serviceimpl;

import com.example.orderservice.data.Category;
import com.example.orderservice.exception.CategoryException;
import com.example.orderservice.modal.CategoryDto;
import com.example.orderservice.modal.Response;
import com.example.orderservice.repository.CategoryRepository;
import com.example.orderservice.service.CategoryService;
import com.example.orderservice.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    Environment environment;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Object addCategory(CategoryDto categoryDto) {
        log.info("Inside addCategory of CategoryServiceImpl");
        try {
            validateAddCategory(categoryDto);
            Category savedCategory = categoryRepository.save(populateCategoryFromCategoryDto(categoryDto));
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CATEGORY_ADDED_SUCCESSFULLY), savedCategory);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CategoryException) {
                errorMessage = ((CategoryException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in addCategory of CategoryServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CategoryException(errorMessage);
        }
    }

    @Override
    public Object updateCategory(CategoryDto categoryDto) {
        log.info("Inside updateCategory of CategoryServiceImpl");
        try {
            Category category = validateUpdateCategory(categoryDto);
            Category updatedCategory = categoryRepository.save(populateCategoryFromCategoryDtoForUpdate(categoryDto, category));
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CATEGORY_UPDATED_SUCCESSFULLY), updatedCategory);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CategoryException) {
                errorMessage = ((CategoryException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in updateCategory of CategoryServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CategoryException(errorMessage);
        }
    }

    @Override
    public Object deleteCategory(Long categoryId) {
        log.info("Inside deleteCategory of CategoryServiceImpl");
        try {
            validateDeleteCategory(categoryId);
            categoryRepository.deleteById(categoryId);
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CATEGORY_DELETED_SUCCESSFULLY));
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CategoryException) {
                errorMessage = ((CategoryException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in deleteCategory of CategoryServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CategoryException(errorMessage);
        }
    }

    @Override
    public Object getCategoryById(Long categoryId) {
        log.info("Inside getCategoryById of CategoryServiceImpl");
        try {
            validateGetCategoryByID(categoryId);
            Optional<Category> dataFromRepo = categoryRepository.findById(categoryId);
            if (!dataFromRepo.isPresent()) {
                log.info("No category found");
                throw new CategoryException(environment.getProperty(Constants.NO_DATA_FOUND));
            }
            Category category = dataFromRepo.get();
            CategoryDto categoryDto = poupulateCategoryDtoFromCategory(category);
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.CATEGORY_FETCHED_SUCCESSFULLY), categoryDto);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CategoryException) {
                errorMessage = ((CategoryException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getCategoryById of CategoryServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CategoryException(errorMessage);
        }
    }

    @Override
    public Object getAllCategory() {
        log.info("Inside getAllCategory of CategoryServiceImpl");
        try {
            List<Category> dataFromRepo = categoryRepository.findAll();
            if (dataFromRepo == null || dataFromRepo.size() == 0) {
                log.info("No Data found");
                throw new CategoryException(environment.getProperty(Constants.NO_DATA_FOUND));
            }
            List<CategoryDto> responseList = dataFromRepo.stream().map(x -> {
                try {
                    return poupulateCategoryDtoFromCategory(x);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            return new Response<>(environment.getProperty(Constants.SUCCESS_CODE), environment.getProperty(Constants.ALL_CATEGORY_FETCHED_SUCCESSFULLY), responseList);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof CategoryException) {
                errorMessage = ((CategoryException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getAllCategory of CategoryServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new CategoryException(errorMessage);
        }
    }

    public void validateAddCategory(CategoryDto categoryDto) throws Exception {
        log.info("Inside validateAddCategory of CategoeyServiceImpl");
        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) {
            log.info("Invalid Request Data");
            throw new CategoryException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        if (categoryRepository.findByCategoryName(categoryDto.getCategoryName()) != null) {
            log.info("Category already exist");
            log.info("Environment is :{}", environment.toString());
            throw new CategoryException(environment.getProperty(Constants.CATEGORY_ALREADY_EXIST));
        }
    }

    public Category validateUpdateCategory(CategoryDto categoryDto) throws Exception {
        log.info("Inside validateUpdateCategory of CategoeyServiceImpl");
        if (categoryDto.getCategoryId() == null) {
            log.info("Invalid Request Data");
            throw new CategoryException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        Optional<Category> category = categoryRepository.findById(categoryDto.getCategoryId());
        if (!category.isPresent()) {
            log.info("Category does not exist");
            throw new CategoryException(environment.getProperty(Constants.CATEGORY_DOES_NOT_EXIST));
        }
        if (categoryRepository.findByCategoryName(categoryDto.getCategoryName()) != null) {
            log.info("Category already exist");
            log.info("Environment is :{}", environment.toString());
            throw new CategoryException(environment.getProperty(Constants.CATEGORY_ALREADY_EXIST));
        }
        return category.get();
    }

    public Category validateDeleteCategory(Long categoryId) throws Exception {
        log.info("Inside validateUpdateCategory of CategoeyServiceImpl");
        if (categoryId == null) {
            log.info("Invalid Request Data");
            throw new CategoryException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            log.info("Category does not exist");
            throw new CategoryException(environment.getProperty(Constants.CATEGORY_DOES_NOT_EXIST));
        }
        return category.get();
    }

    public void validateGetCategoryByID(Long categoryId) throws Exception {
        log.info("Inside validateGetCategoryByID of CategoeyServiceImpl");
        if (categoryId == null) {
            log.info("Invalid Request Data");
            throw new CategoryException(environment.getProperty(Constants.INVALID_REQUEST_DATA));
        }
    }


    public Category populateCategoryFromCategoryDto(CategoryDto categoryDto) throws Exception {
        log.info("Inside populateCategoryFromCategoryDto of CategoeyServiceImpl");
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return category;
    }

    public Category populateCategoryFromCategoryDtoForUpdate(CategoryDto categoryDto, Category category) throws Exception {
        log.info("Inside populateCategoryFromCategoryDtoForUpdate of CategoeyServiceImpl");
        category.setCategoryId(categoryDto.getCategoryId());
        category.setCategoryName(categoryDto.getCategoryName());
        return category;
    }

    public CategoryDto poupulateCategoryDtoFromCategory(Category category) throws Exception {
        log.info("Inside poupulateCategoryDtoFromCategory of CategoeyServiceImpl");
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }

}
