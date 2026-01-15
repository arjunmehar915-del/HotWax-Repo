package com.ecommerce.api.controller;

import com.ecommerce.api.dto.OrderItemRequest;
import com.ecommerce.api.dto.OrderRequest;
import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.*;
import com.ecommerce.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactMechRepository contactMechRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        OrderHeader order = new OrderHeader();
        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        
        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        if (customer.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found");
        order.setCustomer(customer.get());

        Optional<ContactMech> shipping = contactMechRepository.findById(request.getShippingContactMechId());
        if (shipping.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shipping Contact not found");
        order.setShippingContactMech(shipping.get());

        Optional<ContactMech> billing = contactMechRepository.findById(request.getBillingContactMechId());
        if (billing.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Billing Contact not found");
        order.setBillingContactMech(billing.get());

        List<OrderItem> items = new ArrayList<>();
        if (request.getOrderItems() != null) {
            for (OrderItemRequest itemReq : request.getOrderItems()) {
                OrderItem item = new OrderItem();
                Optional<Product> prod = productRepository.findById(itemReq.getProductId());
                if (prod.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found: " + itemReq.getProductId());
                item.setProduct(prod.get());
                item.setQuantity(itemReq.getQuantity());
                item.setStatus(itemReq.getStatus());
                items.add(item);
            }
        }
        order.setOrderItems(items);

        OrderHeader created = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderHeader> getOrder(@PathVariable Integer id) {
        OrderHeader order = orderService.getOrder(id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @RequestBody OrderRequest request) {
        OrderHeader updates = new OrderHeader();
        if (request.getShippingContactMechId() != null) {
             Optional<ContactMech> m = contactMechRepository.findById(request.getShippingContactMechId());
             m.ifPresent(updates::setShippingContactMech);
        }
        if (request.getBillingContactMechId() != null) {
             Optional<ContactMech> m = contactMechRepository.findById(request.getBillingContactMechId());
             m.ifPresent(updates::setBillingContactMech);
        }
        
        OrderHeader updated = orderService.updateOrder(id, updates);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<?> addOrderItem(@PathVariable Integer id, @RequestBody OrderItemRequest request) {
        OrderItem item = new OrderItem();
        Optional<Product> prod = productRepository.findById(request.getProductId());
        if (prod.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
        item.setProduct(prod.get());
        item.setQuantity(request.getQuantity());
        item.setStatus(request.getStatus());

        OrderItem created = orderService.addOrderItem(id, item);
        if (created == null) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/items/{seqId}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Integer id, @PathVariable Integer seqId, @RequestBody OrderItemRequest request) {
        OrderItem updates = new OrderItem();
        updates.setQuantity(request.getQuantity());
        updates.setStatus(request.getStatus());

        try {
            OrderItem updated = orderService.updateOrderItem(id, seqId, updates);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/items/{seqId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Integer id, @PathVariable Integer seqId) {
        try {
            orderService.deleteOrderItem(id, seqId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
