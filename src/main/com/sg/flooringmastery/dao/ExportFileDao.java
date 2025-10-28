package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExportFileDao implements ExportFileDaoInterface {
    private List<Order> allOrders;
    private static final String EXPORT_FILE = "";
    private static final String DELIMITER = "";

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> orders) throws PersistenceException {

    }

    private void writeToFile() throws PersistenceException {

    }
}
