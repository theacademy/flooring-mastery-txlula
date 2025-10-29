package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class ExportFileDao implements ExportFileDaoInterface {
    private List<Order> allOrders;
    private static final String EXPORT_FILE = "backup/DataExport.txt";
    private static final String DELIMITER = ",";

    @Override
    public void exportData(Map<LocalDate, Map<Integer, Order>> orders) throws PersistenceException {

    }

    private void writeToFile() throws PersistenceException {

    }
}
