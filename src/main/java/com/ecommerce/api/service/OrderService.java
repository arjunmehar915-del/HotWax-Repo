package com.ecommerce.api.service;

import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactMechRepository contactMechRepository;

    @Autowired
    private ProductRepository productRepository;

    // Create Order
    @Transactional
    public OrderHeader createOrder(OrderHeader order) {

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }


    public OrderHeader getOrder(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }


    @Transactional
    public OrderHeader updateOrder(Integer orderId, OrderHeader orderDetails) {
        Optional<OrderHeader> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderHeader order = optionalOrder.get();

            if(orderDetails.getShippingContactMech() != null) order.setShippingContactMech(orderDetails.getShippingContactMech());
            if(orderDetails.getBillingContactMech() != null) order.setBillingContactMech(orderDetails.getBillingContactMech());
            return orderRepository.save(order);
        }
        return null;
    }


    @Transactional
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }


    @Transactional
    public OrderItem addOrderItem(Integer orderId, OrderItem item) {
        Optional<OrderHeader> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            item.setOrder(optionalOrder.get());
            return orderItemRepository.save(item);
        }
        return null;
    }

    @Transactional
    public OrderItem updateOrderItem(Integer orderId, Integer seqId, OrderItem itemDetails) {
        Optional<OrderItem> optionalItem = orderItemRepository.findById(seqId);
        if (optionalItem.isPresent()) {
            OrderItem item = optionalItem.get();
            if (!item.getOrder().getOrderId().equals(orderId)) {
                throw new IllegalArgumentException("Item does not belong to order " + orderId);
            }
            if(itemDetails.getQuantity() != null) item.setQuantity(itemDetails.getQuantity());
            if(itemDetails.getStatus() != null) item.setStatus(itemDetails.getStatus());
            return orderItemRepository.save(item);
        }
        return null;
    }

    @Transactional
    public void deleteOrderItem(Integer orderId, Integer seqId) {
        Optional<OrderItem> optionalItem = orderItemRepository.findById(seqId);
        if (optionalItem.isPresent()) {
            if (!optionalItem.get().getOrder().getOrderId().equals(orderId)) {
                 throw new IllegalArgumentException("Item does not belong to order " + orderId);
            }
            orderItemRepository.deleteById(seqId);
        }
    }
}
