package com.example.paymentservicesaga.aggregate;

import com.example.commonservicesaga.commands.CancelPaymentCommand;
import com.example.commonservicesaga.commands.ValidatePaymentCommand;
import com.example.commonservicesaga.event.PaymentCancelledEvent;
import com.example.commonservicesaga.event.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

@Aggregate
@Slf4j
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private Double payableAmount;
    private String paymentMode;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private String paymentStatus;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        log.info("Executing ValidatePaymentCommand for " +
                        "Order Id: {} and Payment Id: {}",
                validatePaymentCommand.getOrderId(),
                validatePaymentCommand.getPaymentId());
        PaymentProcessedEvent paymentProcessedEvent
                = new PaymentProcessedEvent();
        paymentProcessedEvent.setPaymentId(validatePaymentCommand.getPaymentId());
        paymentProcessedEvent.setPaymentMode(validatePaymentCommand.getPaymentMode());
        paymentProcessedEvent.setOrderId(validatePaymentCommand.getOrderId());
        paymentProcessedEvent.setPayableAmount(validatePaymentCommand.getPayableAmount());
        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("PaymentProcessedEvent Applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
        this.createdOn=event.getCreatedOn();
        this.modifiedOn=event.getModifiedOn();
        this.payableAmount=event.getPayableAmount();
        this.paymentMode=event.getPaymentMode();
        this.paymentStatus=event.getPaymentStatus();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {
        PaymentCancelledEvent paymentCancelledEvent
                = new PaymentCancelledEvent();
        BeanUtils.copyProperties(cancelPaymentCommand,
                paymentCancelledEvent);

        AggregateLifecycle.apply(paymentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}