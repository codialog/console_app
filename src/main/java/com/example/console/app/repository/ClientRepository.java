package com.example.console.app.repository;

import com.example.console.app.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CommonRepository<Customer> {
    List<Customer> findAllByLastName(String lastName);
}
