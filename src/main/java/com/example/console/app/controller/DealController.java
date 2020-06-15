package com.example.console.app.controller;

import com.example.console.app.contstants.JsonProperties;
import com.example.console.app.model.Customer;
import com.example.console.app.model.Deal;
import com.example.console.app.model.Item;
import com.example.console.app.service.DealService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class DealController {

    private final DealService dealService;


    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    public JSONObject getJsonStat(LocalDate startDate, LocalDate endDate){
        List<Deal> deals = getStat(startDate, endDate);

        Map<String, Double> expensesMap = new HashMap<>();
        Map <String, List<Item>> dealsMap = new HashMap<>();

        deals.stream().forEach(deal -> {
            Customer customer = deal.getCustomer();
            String nameCustomer = String.join(" ", customer.getLastName(), customer.getName());
            Item item = deal.getItem();
            Double expenses = item.getExpenses();
            if (expensesMap.containsKey(nameCustomer)){
                expenses += expensesMap.get(nameCustomer);
            }
            expensesMap.put(nameCustomer, expenses);
            List<Item> items = new ArrayList<>();
            if (dealsMap.containsKey(nameCustomer)){
                items = dealsMap.get(nameCustomer);
            }
            items.add(item);
            dealsMap.put(nameCustomer, items);
        });

        JSONArray customers = new JSONArray();
        dealsMap.entrySet().stream().forEach(deal -> {
            JSONObject customer = new JSONObject();
            customer.put(JsonProperties.NAME, deal.getKey());
            JSONArray purchases = new JSONArray();
            deal.getValue().stream().forEach(item -> {
                JSONObject purchase = new JSONObject();
                purchase.put(JsonProperties.NAME, item.getName());
                purchase.put(JsonProperties.EXPENSES, item.getExpenses());
                purchases.add(purchase);
            });
            customer.put(JsonProperties.PURCHASES, purchases);
            customer.put(JsonProperties.TOTAL_EXPENSES, expensesMap.get(deal.getKey()));
            customers.add(customer);
        });

        LocalDate day = startDate;
        int totalDays = 0;
        Long allDays = DAYS.between(startDate, endDate);
        while (allDays != 0) {
            if (!(day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                totalDays++;
            }
            day = day.plusDays(1);
            allDays--;
        }


        Double totalExpenses = expensesMap.values().stream().mapToDouble(v -> v).sum();

        JSONObject output = new JSONObject();
        output.put(JsonProperties.TYPE, JsonProperties.STAT);
        output.put(JsonProperties.TOTAL_DAYS, totalDays);
        output.put(JsonProperties.CUSTOMERS, customers);
        output.put(JsonProperties.TOTAL_EXPENSES, totalExpenses);
        output.put(JsonProperties.AVG_EXPENSES, totalExpenses/expensesMap.size());

        return output;
    }

    public List<Deal> getStat(LocalDate startDate, LocalDate endDate) {
        List<Deal> deals = dealService.findAllByDateBetween(startDate, endDate);
        deals.removeIf(Deal::isBoughtOnWeekend);
        return deals;
    }

}
