package com.example.orderservice.saga;

import com.example.commonservicesaga.commands.*;
import com.example.commonservicesaga.event.*;
import com.example.commonservicesaga.modals.User;
import com.example.commonservicesaga.modals.UserDto;
import com.example.commonservicesaga.query.GetUserPaymentDetailsQuery;
import com.example.orderservice.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Lazy
    @Autowired
    private transient CommandGateway commandGateway;

    @Lazy
    @Autowired
    private transient QueryGateway queryGateway;


    public OrderProcessingSaga() {
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order Id : {}",
                event.getOrderId());
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
                = new GetUserPaymentDetailsQuery(String.valueOf(event.getUserId()));
        boolean flag=false;
        UserDto user = null;
        try {
            user = queryGateway.query(
                    getUserPaymentDetailsQuery,
                    ResponseTypes.instanceOf(UserDto.class)
            ).join();

        } catch (Exception e) {
            flag=true;
            log.error("Error in OrderCreatedEvent");
            log.error(e.getMessage());
            //Start the Compensating transaction
           cancelOrderCommand(event.getOrderId());
        }
        if(!flag) {
            ValidatePaymentCommand validatePaymentCommand
                    = ValidatePaymentCommand
                    .builder()
                    .orderId(event.getOrderId())
                    .paymentId(UUID.randomUUID().toString())
                    .payableAmount(event.getTotalAmount())
                    .paymentMode("CASH")
                    .build();
            commandGateway.sendAndWait(validatePaymentCommand);
        }
    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand
                = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for Order Id : {}",
                event.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand
                    = ShipOrderCommand
                    .builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error("Error in PaymentProcessedEvent");
            log.error(e.getMessage());
            // Start the compensating transaction
            cancelPaymentCommand(event);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event) {
        CancelPaymentCommand cancelPaymentCommand
                = new CancelPaymentCommand(
                event.getPaymentId(), event.getOrderId()
        );
        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {

        log.info("OrderShippedEvent in Saga for Order Id : {}",
                event.getOrderId());
        CompleteOrderCommand completeOrderCommand
                = CompleteOrderCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus("APPROVED")
                .build();
        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order Id : {}",
                event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in Saga for Order Id : {}",
                event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in Saga for Order Id : {}",
                event.getOrderId());
      cancelOrderCommand(event.getOrderId());
    }
}
