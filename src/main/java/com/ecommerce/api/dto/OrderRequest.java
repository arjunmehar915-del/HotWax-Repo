package com.ecommerce.api.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {
    private LocalDate orderDate;
    private Integer customerId;
    private Integer shippingContactMechId;
    private Integer billingContactMechId;
    private List<OrderItemRequest> orderItems;
}
