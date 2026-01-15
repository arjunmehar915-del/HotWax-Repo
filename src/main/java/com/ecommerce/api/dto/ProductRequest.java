package com.ecommerce.api.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String color;
    private String size;
}
