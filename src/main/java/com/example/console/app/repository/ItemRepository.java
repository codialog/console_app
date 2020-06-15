package com.example.console.app.repository;

import com.example.console.app.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CommonRepository<Item> {
}
