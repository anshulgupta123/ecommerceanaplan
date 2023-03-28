package com.example.orderservice.events;

import com.example.commonservicesaga.event.OrderCancelledEvent;
import com.example.commonservicesaga.event.OrderCompletedEvent;
import com.example.orderservice.data.Order;
import com.example.orderservice.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class OrderEventsHandler {

    @Autowired
    @Lazy
    private OrderRepository orderRepository;


    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = new Order();
        order.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        order.setModifiedOn(new Timestamp(System.currentTimeMillis()));
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).get();
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).get();
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }
}

