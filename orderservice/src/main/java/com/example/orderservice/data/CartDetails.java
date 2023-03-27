package com.example.orderservice.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "cartdetails")
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartDetailsId;

    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cartId;
    private Long productId;
    private Integer quantity;
    private Double productPrice;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
}
