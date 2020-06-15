package com.example.console.app.service;

import com.example.console.app.model.Customer;
import com.example.console.app.model.Deal;
import com.example.console.app.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class DealService extends AbstractService<Deal, DealRepository> {

    protected DealService(DealRepository repository) {
        super(repository);
    }

    public List<Customer> findAllClientByItem_Name(String name) {
        return repository.findAllClientByItem_Name(name);
    }

    public List<Deal> findAllByDateBetween(LocalDate dateStart, LocalDate dateEnd) {
        return repository.findAllByDateBetween(dateStart, dateEnd);
    }
}
