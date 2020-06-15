package com.example.console.app.repository;

import com.example.console.app.model.Customer;
import com.example.console.app.model.Deal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DealRepository extends CommonRepository<Deal> {

    @Query("SELECT deal.customer FROM Deal deal WHERE LOWER(deal.item.name) = LOWER(:name)")
    List<Customer> findAllClientByItem_Name(@Param("name") String name);

    List<Deal> findAllByDateBetween(LocalDate dateStart, LocalDate dateEnd);
}
