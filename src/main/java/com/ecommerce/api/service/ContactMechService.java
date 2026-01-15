package com.ecommerce.api.service;

import com.ecommerce.api.model.ContactMech;
import com.ecommerce.api.repository.ContactMechRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactMechService {

    @Autowired
    private ContactMechRepository contactMechRepository;

    public ContactMech createContactMech(ContactMech contactMech) {
        return contactMechRepository.save(contactMech);
    }

    public ContactMech getContactMech(Integer id) {
        return contactMechRepository.findById(id).orElse(null);
    }

    public List<ContactMech> getAllContactMechs() {
        return contactMechRepository.findAll();
    }

    public ContactMech updateContactMech(Integer id, ContactMech updates) {
        Optional<ContactMech> existingOpt = contactMechRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }
        ContactMech existing = existingOpt.get();
        if (updates.getStreetAddress() != null)
            existing.setStreetAddress(updates.getStreetAddress());
        if (updates.getCity() != null)
            existing.setCity(updates.getCity());
        if (updates.getState() != null)
            existing.setState(updates.getState());
        if (updates.getPostalCode() != null)
            existing.setPostalCode(updates.getPostalCode());
        if (updates.getPhoneNumber() != null)
            existing.setPhoneNumber(updates.getPhoneNumber());
        if (updates.getEmail() != null)
            existing.setEmail(updates.getEmail());

        return contactMechRepository.save(existing);
    }

    public void deleteContactMech(Integer id) {
        contactMechRepository.deleteById(id);
    }
}
