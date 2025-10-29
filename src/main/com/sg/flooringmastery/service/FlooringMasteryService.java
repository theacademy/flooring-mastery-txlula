package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.exceptions.NoSuchOrderException;
import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class FlooringMasteryService implements FlooringMasteryServiceInterface {
    private OrderFileDao orderFileDao;
    private AuditFileDao auditFileDao;
    private ProductFileDao productFileDao;
    private TaxFileDao taxFileDao;
    private ExportFileDao exportFileDao;

    @Autowired
    public FlooringMasteryService(OrderFileDao orderFileDao, AuditFileDao auditFileDao, ProductFileDao productFileDao, TaxFileDao taxFileDao, ExportFileDao exportFileDao) {
        this.orderFileDao = orderFileDao;
        this.auditFileDao = auditFileDao;
        this.productFileDao = productFileDao;
        this.taxFileDao = taxFileDao;
        this.exportFileDao = exportFileDao;
    }

    @Override
    public int getNextOrderNumber() throws PersistenceException, NoSuchOrderException {
        return orderFileDao.getNextOrderNumber();
    }

    @Override
    public Order addOrder(Order order) throws PersistenceException {
        auditFileDao.writeAuditEntry("CREATED: Order #" + order.getOrderNumber());
        return orderFileDao.addOrder(order);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        return orderFileDao.getOrder(date, orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order order) throws PersistenceException, NoSuchOrderException {
        auditFileDao.writeAuditEntry("UPDATED: Order #" + order.getOrderNumber());
        return orderFileDao.editOrder(date, orderNumber, order);
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException, NoSuchOrderException {
        return orderFileDao.getOrdersForDate(date);
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        auditFileDao.writeAuditEntry("REMOVED: Order #" + orderNumber);
        return orderFileDao.removeOrder(date, orderNumber);
    }

    @Override
    public void exportData() throws PersistenceException {
        Map<LocalDate, Map<Integer, Order>> orders = orderFileDao.getAllOrders();
        exportFileDao.exportData(orders);
    }

    @Override
    public List<Tax> getTaxes() throws PersistenceException {
        return taxFileDao.getAllTaxes();
    }

    @Override
    public List<Product> getProducts() throws PersistenceException {
        return productFileDao.getAllProducts();
    }
}
