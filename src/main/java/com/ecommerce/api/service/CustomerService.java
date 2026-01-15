package com.ecommerce.api.service;

import com.ecommerce.api.model.Customer;
import com.ecommerce.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Integer id, Customer updates) {
        Optional<Customer> existingOpt = customerRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        Customer existing = existingOpt.get();
        if (updates.getFirstName() != null)
            existing.setFirstName(updates.getFirstName());
        if (updates.getLastName() != null)
            existing.setLastName(updates.getLastName());

        return customerRepository.save(existing);
    }

    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }
}
