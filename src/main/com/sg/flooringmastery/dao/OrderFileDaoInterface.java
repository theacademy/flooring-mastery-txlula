package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderFileDaoInterface {
    int getNextOrderNumber()
        throws PersistenceException;

    Order addOrder(Order order)
        throws PersistenceException;

    Order editOrder(LocalDate date, Order order)
        throws PersistenceException;

    Order removeOrder(LocalDate date, int orderNumber)
        throws PersistenceException;

    Map<LocalDate, Map<Integer, Order>> getAllOrders()
        throws PersistenceException;

    List<Order> getOrdersForDate(LocalDate date)
        throws PersistenceException;

    Order getOrder(LocalDate date, int orderNumber)
        throws PersistenceException;
}
