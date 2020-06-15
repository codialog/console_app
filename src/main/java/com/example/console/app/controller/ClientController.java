package com.example.console.app.controller;

import com.example.console.app.contstants.JsonProperties;
import com.example.console.app.model.Customer;
import com.example.console.app.service.ClientService;
import com.example.console.app.service.DealService;
import com.example.console.app.service.ItemService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ClientController {

    private final ClientService clientService;
    private final DealService dealService;
    private final ItemService itemService;

    @Autowired
    public ClientController(ClientService clientService, DealService dealService, ItemService itemService) {
        this.clientService = clientService;
        this.dealService = dealService;
        this.itemService = itemService;
    }

    public JSONArray search(JSONObject jsonObject) throws Exception {
        JSONArray jsonArray = new JSONArray();
        List<Customer> customers;
        if (jsonObject.containsKey(JsonProperties.LAST_NAME)) {
            String lastName = jsonObject.get(JsonProperties.LAST_NAME).toString();
            customers = search(lastName);
        } else if (jsonObject.containsKey(JsonProperties.MIN_TIMES) && jsonObject.containsKey(JsonProperties.PRODUCT_NAME)) {
            Long minTimes = (Long) jsonObject.get(JsonProperties.MIN_TIMES);
            String productName = jsonObject.get(JsonProperties.PRODUCT_NAME).toString();
            customers = search(productName, minTimes);
        } else if (jsonObject.containsKey(JsonProperties.MIN_EXPENSES) && jsonObject.containsKey(JsonProperties.MAX_EXPENSES)) {
            Long minExpenses = (Long) jsonObject.get(JsonProperties.MIN_EXPENSES);
            Long maxExpenses = (Long) jsonObject.get(JsonProperties.MAX_EXPENSES);
            customers = search(minExpenses, maxExpenses);
        } else if (jsonObject.containsKey(JsonProperties.BAD_CUSTOMERS)) {
            Long badCustomers = (Long) jsonObject.get(JsonProperties.BAD_CUSTOMERS);
            customers = search(badCustomers);
        } else throw new Exception();
        for (Customer customer : customers) {
            JSONObject inner = new JSONObject();
            inner.put(JsonProperties.FIRST_NAME, customer.getName());
            inner.put(JsonProperties.LAST_NAME, customer.getLastName());
            jsonArray.add(inner);
        }
        return jsonArray;
    }

    public List<Customer> search(String lastName) {
        return clientService.search(lastName);
    }

    public List<Customer> search(String productName, Long minTimes) {
        List<Customer> list = dealService.findAllClientByItem_Name(productName);
        Map<Customer, Long> counted = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Set<Map.Entry<Customer, Long>> entries = counted.entrySet();
        return entries.stream()
                .filter(e -> e.getValue() >= minTimes)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Customer> search(Long minExpenses, Long maxExpenses) {
        List<Customer> customers = clientService.getAll();
        Iterator<Customer> clientIterator = customers.iterator();
        while (clientIterator.hasNext()) {
            Customer nextCustomer = clientIterator.next();
            Double expenses = nextCustomer.getExpenses();
            if (minExpenses > expenses || expenses > maxExpenses) {
                clientIterator.remove();
            }
        }
        return customers;
    }

    public List<Customer> search(Long badCustomers) {
        List<Customer> customers = clientService.getAll();
        return customers.stream()
                .sorted(Comparator.comparingInt(c -> c.getDeals().size()))
                .limit(badCustomers)
                .collect(Collectors.toList());
    }
}
