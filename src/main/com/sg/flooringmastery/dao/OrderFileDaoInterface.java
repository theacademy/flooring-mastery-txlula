package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.NoSuchOrderException;
import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderFileDaoInterface {
    int getNextOrderNumber()
        throws PersistenceException,
            NoSuchOrderException;

    Order addOrder(Order order)
        throws PersistenceException;

    Order editOrder(LocalDate date, int orderNumber, Order order)
        throws PersistenceException,
            NoSuchOrderException;

    Order removeOrder(LocalDate date, int orderNumber)
            throws PersistenceException,
            NoSuchOrderException;

    List<Order> getOrdersForDate(LocalDate date)
            throws PersistenceException,
            NoSuchOrderException;

    Order getOrder(LocalDate date, int orderNumber)
            throws PersistenceException,
            NoSuchOrderException;
}
