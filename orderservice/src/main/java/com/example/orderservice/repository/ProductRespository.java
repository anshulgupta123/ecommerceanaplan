package com.example.orderservice.repository;

import com.example.orderservice.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRespository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where (product_name like %?1% or product_price like %?1%)", nativeQuery = true)
    List<Product> findAllPoductBySearchParam(String serachParam);
}
