package com.example.orderservice.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartId;
    private Long userId;
    private Double totalAmount;
    @OneToMany(mappedBy = "cartId")
    private List<CartDetails> cartDetailsList;
}
