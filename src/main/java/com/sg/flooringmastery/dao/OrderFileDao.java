package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.NoSuchOrderException;
import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderFileDao implements OrderFileDaoInterface {
    private static final String DELIMITER = ",";
    private final Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();
    private int largestOrderNumber = 0;

    private String marshallOrder(Order order) {
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
                + order.getTotal();
    }

    private void writeToFile(LocalDate orderDate) throws PersistenceException {
        PrintWriter out;
        String date = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        // write to file with date
        try {
            out = new PrintWriter(new FileWriter("data/Orders_" + date + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String orderText;
            List<Order> orders = this.getOrdersForDate(orderDate);

            for (Order order : orders) {
                // Format order class to text to write to file
                orderText = marshallOrder(order);
                out.println(orderText);
                out.flush();
            }
        } catch (Exception e) {
            throw new PersistenceException("Error: Cannot get orders to write to file.", e);
        }

        out.close();
    }

    // format file text of each line to create a new Order class
    private Order unmarshallOrder(String orderText) {
        String[] orderTokens = orderText.split(DELIMITER);
        Order order = new Order();

        int orderNumber = Integer.parseInt(orderTokens[0]);
        order.setOrderNumber(orderNumber);

        String customerName = orderTokens[1];
        order.setCustomerName(customerName);

        String state = orderTokens[2];
        order.setState(state);

        BigDecimal taxRate = BigDecimal.valueOf(Double.parseDouble(orderTokens[3]));
        order.setTaxRate(taxRate);

        String productType = orderTokens[4];
        order.setProductType(productType);

        BigDecimal area = BigDecimal.valueOf(Double.parseDouble(orderTokens[5]));
        order.setArea(area);

        BigDecimal costPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(orderTokens[6]));
        order.setCostPerSquareFoot(costPerSquareFoot);

        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(Double.parseDouble(orderTokens[7]));
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);

        BigDecimal materialCost = BigDecimal.valueOf(Double.parseDouble(orderTokens[8]));
        order.setMaterialCost(materialCost);

        BigDecimal laborCost = BigDecimal.valueOf(Double.parseDouble(orderTokens[9]));
        order.setLaborCost(laborCost);

        BigDecimal tax = BigDecimal.valueOf(Double.parseDouble(orderTokens[10]));
        order.setTax(tax);

        BigDecimal total = BigDecimal.valueOf(Double.parseDouble(orderTokens[11]));
        order.setTotal(total);

        return order;
    }

    private void loadFromFile(LocalDate orderDate) throws PersistenceException {
        Scanner scanner;
        String date = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        try {
            scanner = new Scanner(new BufferedReader(new FileReader("data/Orders_" + date + ".txt")));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Error: could not load from file / no orders on that date.");
        }

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            Order currentOrder = unmarshallOrder(currentLine);
            currentOrder.setOrderDate(orderDate);

            // if date exists as a key on Hashmap, create new Order Hashmap for that date
            if (orders.get(currentOrder.getOrderDate()) != null) {
                orders.get(currentOrder.getOrderDate()).put(currentOrder.getOrderNumber(), currentOrder);
            }
            else {
                // if date does not exist as a key on Hashmap, create new Hashmap for that date and new Order Hashmap
                orders.put(currentOrder.getOrderDate(), new HashMap<>() {{
                    put(currentOrder.getOrderNumber(), currentOrder);
                }});
            }
        }

        scanner.close();
    }

    @Override
    public int getNextOrderNumber() throws PersistenceException, NoSuchOrderException {
        List<Order> orders = this.getOrdersForDate(LocalDate.now());
        if (!orders.isEmpty()) {
            // Based on number of orders in file, get largest order number
            Order order = orders.get(orders.size() - 1);
            largestOrderNumber = order.getOrderNumber() + 1;
        }
        return largestOrderNumber;
    }

    @Override
    public Order addOrder(Order order) throws PersistenceException {
        // Check if file exists for the date
        this.validateFile(order.getOrderDate());

        // if date exists as a key on Hashmap, create new Order Hashmap for that date
        if (orders.get(order.getOrderDate()) != null) {
            orders.get(order.getOrderDate()).put(order.getOrderNumber(), order);
        }
        else {
            // if date does not exist as a key on Hashmap, create new Hashmap for that date and new Order Hashmap
            orders.put(order.getOrderDate(), new HashMap<>() {{
                put(order.getOrderNumber(), order);
            }});
        }

        writeToFile(order.getOrderDate());

        largestOrderNumber++;

        return orders.get(order.getOrderDate()).get(order.getOrderNumber());
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        // check if file exists for the date
        this.validateFile(date);

        // check if order exists
        try {
            if (orders.get(date) != null) {
                return orders.get(date).get(orderNumber);
            }
            return new Order();
        }
        catch (Exception e) {
            throw new NoSuchOrderException("Error: cannot get order.", e);
        }
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order order) throws PersistenceException, NoSuchOrderException {
        // check if file exists for the date
        this.validateFile(date);

        try {
            // if date exists as a key on Hashmap, update Order Hashmap for that date
            if (orders.get(order.getOrderDate()) != null) {
                orders.get(order.getOrderDate()).put(order.getOrderNumber(), order);
            }
            else {
                // if date does not exist as a key on Hashmap, create new Hashmap for that date and update Order Hashmap
                orders.put(order.getOrderDate(), new HashMap<>() {{
                    put(order.getOrderNumber(), order);
                }});
            }

            writeToFile(order.getOrderDate());
            return orders.get(order.getOrderDate()).get(order.getOrderNumber());
        } catch (Exception e) {
            throw new NoSuchOrderException("Error: cannot edit order.", e);
        }
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException {
        return orders;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws PersistenceException, NoSuchOrderException {
        this.validateFile(date);

        // Check if orders exist for the date
        try {
            if (orders.get(date) != null) {
                return orders.get(date).values().stream().toList();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new NoSuchOrderException("Error: cannot get orders from date.", e);
        }
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        this.validateFile(date);

        // Check if order exists to be removed
        try {
            Order order = orders.get(date).remove(orderNumber);
            writeToFile(date);
            return order;
        } catch (Exception e) {
            throw new NoSuchOrderException("Error: cannot remove order.", e);
        }
    }

    private void validateFile(LocalDate date) throws PersistenceException {
        String dateString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        File file = new File("data/Orders_" + dateString + ".txt");

        // Check if file exists for the date
        if (file.isFile()) {
            loadFromFile(date);
        }
    }
}
