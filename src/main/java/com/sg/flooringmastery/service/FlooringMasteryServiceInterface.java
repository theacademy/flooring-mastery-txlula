package com.sg.flooringmastery.service;

import com.sg.flooringmastery.exceptions.NoSuchOrderException;
import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Tax;
import com.sg.flooringmastery.model.Product;

import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryServiceInterface {
    int getNextOrderNumber()
            throws PersistenceException,
            NoSuchOrderException;

    Order addOrder(Order order)
        throws PersistenceException;

    Order getOrder(LocalDate date, int orderNumber)
            throws PersistenceException,
            NoSuchOrderException;

    Order editOrder(LocalDate date, int orderNumber, Order order)
            throws PersistenceException,
            NoSuchOrderException;

    List<Order> getOrdersForDate(LocalDate date)
            throws PersistenceException,
            NoSuchOrderException;

    Order removeOrder(LocalDate date, int orderNumber)
            throws PersistenceException,
            NoSuchOrderException;

    void exportData()
            throws PersistenceException;

    List<Tax> getTaxes()
        throws PersistenceException;

    List<Product> getProducts()
        throws PersistenceException;
}
