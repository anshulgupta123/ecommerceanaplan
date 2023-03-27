package com.example.orderservice.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;
    private Double productPrice;

    @OneToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private Integer productCount;


}
