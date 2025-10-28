package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.Tax;
import com.sg.flooringmastery.service.FlooringMasteryService;
import com.sg.flooringmastery.view.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringMasteryController {
    private final FlooringMasteryService service;
    private final FlooringMasteryView view;

    @Autowired
    public FlooringMasteryController(FlooringMasteryService service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean running = true;
        int userInput = 0;

        try {
            while (running) {
                userInput = getMenuSelection();

                switch (userInput) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        unknownCommand();
                        break;
                }
            }

            exitMessage();
        }
        catch (PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.displayMenuAndGetSelection();
    }

    private void displayOrders() throws PersistenceException {
        LocalDate date = view.getDateInput();
        List<Order> orders = service.getOrdersForDate(date);
        view.displayOrders(date, orders);
    }

    private void addOrder() throws PersistenceException {
        view.displayAddOrderBanner();

        List<Tax> taxes = service.getTaxes();
        List<Product> products = service.getProducts();

        Order order = view.getAddOrderInput(taxes, products);
        this.calculateOrder(order);

        boolean confirmation = view.getAddOrderConfirmation();

        if (confirmation) {
            Order addedOrder = service.addOrder(order);

            if (addedOrder != null) {
                view.displayAddOrderSuccess();
            }
        }
    }

    private void editOrder() throws PersistenceException {
        view.displayEditOrderBanner();

        LocalDate date = view.getDateInput();
        int orderNumber = view.getOrderNumberInput();
        Order fetchedOrder = service.getOrder(date, orderNumber);

        if (fetchedOrder == null) {
            view.displayErrorMessage("Could not find order from information provided. Please try again.");
        }
        else {
            List<Tax> taxes = service.getTaxes();
            List<Product> products = service.getProducts();

            Order order = view.getEditOrderInput(fetchedOrder, taxes, products);
            if (!order.getCustomerName().equals(fetchedOrder.getCustomerName())
            || !order.getState().equals(fetchedOrder.getState())
            || !order.getProductType().equals(fetchedOrder.getProductType())
            || !order.getArea().equals(fetchedOrder.getArea())) {
                Order editedOrder = service.editOrder(date, orderNumber, order);

                if (editedOrder != null) {
                    view.displayEditOrderSuccess();
                }
            }
        }
    }

    private void removeOrder() throws PersistenceException {
        view.displayRemoveOrderBanner();

        LocalDate date = view.getDateInput();
        int orderNumber = view.getOrderNumberInput();

        boolean confirmation = view.getRemoveOrderConfirmation();

        if (confirmation) {
            Order fetchedOrder = service.removeOrder(date, orderNumber);

            if (fetchedOrder != null) {
                view.displayRemoveOrderSuccess();
            }
        }
    }

    private void exportData() throws PersistenceException {

    }

    private void exitMessage() {
        view.displayExitMessage();
    }

    private void unknownCommand() {
        view.displayUnknownCommandMessage();
    }

    private void calculateOrder(Order order) {
        BigDecimal area = order.getArea();
        BigDecimal costPerSquareFoot = order.getCostPerSquareFoot();
        BigDecimal materialCost = area.multiply(costPerSquareFoot);
        order.setMaterialCost(materialCost);

        BigDecimal laborCostPerSquareFoot = order.getLaborCostPerSquareFoot();
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot);
        order.setLaborCost(laborCost);

        BigDecimal taxRate = order.getTaxRate();
        BigDecimal tax = materialCost.add(laborCost).multiply(taxRate.divide(new BigDecimal(100)));
        order.setTax(tax);

        BigDecimal total = materialCost.add(laborCost).add(tax);
        order.setTotal(total);
    }
}
