package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderFileDao;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringMasteryService implements FlooringMasteryServiceInterface {
    private OrderFileDao dao;

    @Autowired
    public FlooringMasteryService(OrderFileDao dao) {
        this.dao = dao;
    }


    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public Order addOrder(Order order) {
        return null;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
        return null;
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order order) {
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) {
        return null;
    }

    @Override
    public void exportData() {

    }

    @Override
    public List<Tax> getTaxes() {
        return List.of();
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }

    private String writeToAudit() {

    }
}
