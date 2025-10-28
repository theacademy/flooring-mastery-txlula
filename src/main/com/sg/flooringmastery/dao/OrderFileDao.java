package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class OrderFileDao implements OrderFileDaoInterface {
    private static String DELIMITER = ",";
    private static String ORDER_FOLDER = "";
    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();
    private int largestOrderNumber = 0;

    private void writeToFile() throws PersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter("Orders_" + ));
        }
        catch (IOException e) {
            throw new PersistenceException("Error: Could not write to file.");
        }

        String orderText;

    }

    private Order unmarshallOrder(String orderText) {
        String[] orderTokens = orderText.split(DELIMITER);
        Order order = new Order();

        int orderNumber = Integer.parseInt(orderTokens[0]);
        order.setOrderNumber(orderNumber);

        String customerName = orderTokens[1];
        order.setCustomerName(customerName);

        String state = orderTokens[2];
        order.setState(state);

        BigDecimal taxRate = BigDecimal.valueOf(Integer.parseInt(orderTokens[3]));
        order.setTaxRate(taxRate);

        String productType = orderTokens[4];
        order.setProductType(productType);

        BigDecimal area = BigDecimal.valueOf(Integer.parseInt(orderTokens[5]));
        order.setArea(area);

        BigDecimal costPerSquareFoot = BigDecimal.valueOf(Integer.parseInt(orderTokens[6]));
        order.setCostPerSquareFoot(costPerSquareFoot);

        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(Integer.parseInt(orderTokens[7]));
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);

        BigDecimal materialCost = BigDecimal.valueOf(Integer.parseInt(orderTokens[8]));
        order.setMaterialCost(materialCost);

        BigDecimal laborCost = BigDecimal.valueOf(Integer.parseInt(orderTokens[9]));
        order.setLaborCost(laborCost);

        BigDecimal tax = BigDecimal.valueOf(Integer.parseInt(orderTokens[10]));
        order.setTax(tax);

        BigDecimal total = BigDecimal.valueOf(Integer.parseInt(orderTokens[11]));
        order.setTotal(total);

        return order;
    }

    private void loadFromFile() throws PersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader("Orders_" + )));
        }
        catch (FileNotFoundException e) {
            throw new PersistenceException("Error: could not load from file.");
        }

        String currentLine;
        Order currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.put();
        }

        scanner.close();
    }

    @Override
    public int getNextOrderNumber() throws PersistenceException {
        return largestOrderNumber + 1;
    }

    @Override
    public Order addOrder(Order order) throws PersistenceException {
        loadFromFile();
        Order newOrder = orders.put(order.getOrderDate(), order);
        writeToFile();
        return newOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException {
        loadFromFile();
        return orders.get(date).get(orderNumber);
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws PersistenceException {
        loadFromFile();
        Order updatedOrder = orders.put();
        writeToFile();
        return updatedOrder;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException {
        loadFromFile();
        return orders.get(date).values().stream().toList();
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException {
        loadFromFile();
        return orders;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException {
        loadFromFile();
        Order order = orders.remove(date).get(orderNumber);
        writeToFile();
        return order;
    }
}
