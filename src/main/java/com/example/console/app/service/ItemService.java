package com.example.console.app.service;

import com.example.console.app.model.Item;
import com.example.console.app.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractService<Item, ItemRepository> {

    protected ItemService(ItemRepository repository) {
        super(repository);
    }
}
