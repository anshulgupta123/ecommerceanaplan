package com.example.orderservice.utility;

public interface UrlConstants {
    String ADD_CATEGORY = "/v1/addCategory";
    String UPDATE_CATEGORY = "/v1/updateCategory";
    String DELETE_CATEGORY = "/v1/deleteCategory";
    String GET_CATEGORY_BY_ID = "/v1/getCategoryById";
    String GET_ALL_CATEGORY = "/v1/getAllCategory";

    String ADD_PRODUCT = "/v1/addProduct";
    String UPDATE_PRODUCT = "/v1/updateProduct";
    String DELETE_PRODUCT = "/v1/deleteProduct";
    String GET_PRODUCT_BY_ID = "/v1/getProductById";
    String GET_ALL_PRODUCT_WITH_SEARCH_PARAM = "/v1/getAllProducts";

    String ADD_ITEMS_TO_CART = "/v1/addItemsToCart";
    String GET_CART_BY_ID = "/v1/getCartById";
    String GET_CART_BY_USER_ID="/v1/getCartByUserId";
}
