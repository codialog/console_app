package com.example.console.app.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Table(name = "cistomer")
public class Customer extends AbstractEntity {

    public Customer(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Customer() {
    }

    @JsonView(Views.Common.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.Common.class)
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Deal> deals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public Double getExpenses(){
        return this.deals.stream().mapToDouble(s -> s.getItem().getExpenses()).sum();
    }

    public List<Item> getItems(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Item> items = new ArrayList<>();
        Long allDays = DAYS.between(startDate, endDate);
        if (allDays <= 0) {
            throw new Exception();
        }
        LocalDate day;
        for (Deal deal: deals) {
            day = deal.getDate();
            if (DAYS.between(startDate, day) <= allDays && !deal.isBoughtOnWeekend()) {
                items.add(deal.getItem());
            }
        }
        return items;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
