package com.example.console.app.service;

import com.example.console.app.model.Customer;
import com.example.console.app.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService extends AbstractService<Customer, ClientRepository> {

    protected ClientService(ClientRepository repository) {
        super(repository);
    }

    public List<Customer> search(String lastName) {
        return repository.findAllByLastName(lastName);
    }
}
