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
        // Go through orders for each date and write to external file
        orders.forEach((orderDate, integerOrderMap) -> {
            allOrders = integerOrderMap.values().stream().toList();
            try {
                writeToFile(orderDate);
            } catch (PersistenceException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String marshallOrder(Order order, String date) {
        return order.getOrderNumber() + DELIMITER
                + order.getCustomerName() + DELIMITER
                + order.getState() + DELIMITER
                + order.getTaxRate() + DELIMITER
                + order.getProductType() + DELIMITER
                + order.getArea() + DELIMITER
                + order.getCostPerSquareFoot() + DELIMITER
                + order.getLaborCostPerSquareFoot() + DELIMITER
                + order.getMaterialCost() + DELIMITER
                + order.getLaborCost() + DELIMITER
                + order.getTax() + DELIMITER
                + order.getTotal() + DELIMITER
                + date;
    }

    private void writeToFile(LocalDate orderDate) throws PersistenceException {
        PrintWriter out;
        String date = orderDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        try {
            out = new PrintWriter(new FileWriter(EXPORT_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String orderText;
            // For each order, write to file
            for (Order order : allOrders) {
                // Format order class to text for file
                orderText = marshallOrder(order, date);
                out.println(orderText);
                out.flush();
            }
        } catch (Exception e) {
            throw new PersistenceException("Error: Cannot get orders to write to file.", e);
        }

        out.close();
    }
}
