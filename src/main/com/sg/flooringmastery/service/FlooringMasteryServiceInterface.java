package com.sg.flooringmastery.service;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Tax;
import com.sg.flooringmastery.model.Product;

import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryServiceInterface {
    int getNextOrderNumber();
    Order addOrder(Order order);
    Order getOrder(LocalDate date, int orderNumber);
    Order editOrder(LocalDate date, int orderNumber, Order order);
    List<Order> getOrdersForDate(LocalDate date);
    Order removeOrder(LocalDate date, int orderNumber);
    void exportData();
    List<Tax> getTaxes();
    List<Product> getProducts();
}
