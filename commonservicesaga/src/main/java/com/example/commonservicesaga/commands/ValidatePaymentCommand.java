package com.example.commonservicesaga.commands;

import com.example.commonservicesaga.modals.CardDetails;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.sql.Timestamp;

@Data
@Builder
public class ValidatePaymentCommand {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private Double payableAmount;
    private String paymentMode;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private String paymentStatus;
}
