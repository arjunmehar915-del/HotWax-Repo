package com.ecommerce.api.dto;

import lombok.Data;

@Data
public class ContactMechRequest {
    private Integer customerId;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
    private String email;
}
