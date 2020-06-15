package com.example.console.app.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item")
public class Item extends AbstractEntity {

    public Item() {
    }

    public Item(String name, Double expenses) {
        this.name = name;
        this.expenses = expenses;
    }

    @JsonView(Views.Common.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.Common.class)
    @Column(name = "expenses")
    private Double expenses;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Deal> deals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }
}
