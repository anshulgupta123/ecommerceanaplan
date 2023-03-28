package com.example.orderservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.sql.Timestamp;


@Builder
@Data
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private Double totalAmount;
    private Long userId;
    private String orderStatus;
    private Timestamp createdOn;
    private Timestamp modifiedOn;

}
