package com.ecommerce.api.controller;

import com.ecommerce.api.dto.ContactMechRequest;
import com.ecommerce.api.model.ContactMech;
import com.ecommerce.api.model.Customer;
import com.ecommerce.api.repository.CustomerRepository;
import com.ecommerce.api.service.ContactMechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact-mechs")
public class ContactMechController {

    @Autowired
    private ContactMechService contactMechService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<?> createContactMech(@RequestBody ContactMechRequest request) {
        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found");
        }

        ContactMech contactMech = new ContactMech();
        contactMech.setCustomer(customer.get());
        contactMech.setStreetAddress(request.getStreetAddress());
        contactMech.setCity(request.getCity());
        contactMech.setState(request.getState());
        contactMech.setPostalCode(request.getPostalCode());
        contactMech.setPhoneNumber(request.getPhoneNumber());
        contactMech.setEmail(request.getEmail());

        ContactMech created = contactMechService.createContactMech(contactMech);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactMech> getContactMech(@PathVariable Integer id) {
        ContactMech contactMech = contactMechService.getContactMech(id);
        if (contactMech == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contactMech);
    }

    @GetMapping
    public ResponseEntity<List<ContactMech>> getAllContactMechs() {
        return ResponseEntity.ok(contactMechService.getAllContactMechs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactMech> updateContactMech(@PathVariable Integer id,
            @RequestBody ContactMechRequest request) {
        ContactMech updates = new ContactMech();
        updates.setStreetAddress(request.getStreetAddress());
        updates.setCity(request.getCity());
        updates.setState(request.getState());
        updates.setPostalCode(request.getPostalCode());
        updates.setPhoneNumber(request.getPhoneNumber());
        updates.setEmail(request.getEmail());

        ContactMech updated = contactMechService.updateContactMech(id, updates);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactMech(@PathVariable Integer id) {
        contactMechService.deleteContactMech(id);
        return ResponseEntity.noContent().build();
    }
}
