package com.ecommerce.api.repository;

import com.ecommerce.api.model.ContactMech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMechRepository extends JpaRepository<ContactMech, Integer> {
}
