package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.exceptions.NoSuchOrderException;
import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.Tax;
import com.sg.flooringmastery.service.FlooringMasteryServiceInterface;
import com.sg.flooringmastery.view.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringMasteryController {
    private final FlooringMasteryServiceInterface service;
    private final FlooringMasteryView view;

    @Autowired
    public FlooringMasteryController(FlooringMasteryServiceInterface service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    // Run menu until user selects exit option
    public void run() {
        boolean running = true;
        int userInput;

        while (running) {
            try {
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
            catch (PersistenceException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }

        exitMessage();
    }

    private int getMenuSelection() {
        return view.displayMenuAndGetSelection();
    }

    private void displayOrders() throws PersistenceException, NoSuchOrderException {
        LocalDate date = view.getDateInput();
        List<Order> orders = service.getOrdersForDate(date);

        if (!orders.isEmpty()) {
            view.displayOrders(date, orders);
        }
        else {
            view.displayNoOrders();
        }
    }

    private void addOrder() throws PersistenceException, NoSuchOrderException {
        view.displayAddOrderBanner();

        // Fetch list of taxes and products
        List<Tax> taxes = service.getTaxes();
        List<Product> products = service.getProducts();

        Order order = view.getAddOrderInput(taxes, products);
        this.calculateOrder(order);

        boolean confirmation = view.getAddOrderConfirmation();

        // Confirm if user wants to place order
        if (confirmation) {
            order.setOrderDate(LocalDate.now());
            order.setOrderNumber(service.getNextOrderNumber());
            Order addedOrder = service.addOrder(order);

            if (addedOrder != null) {
                view.displayAddOrderSuccess();
            }
        }
    }

    private void editOrder() throws PersistenceException, NoSuchOrderException {
        view.displayEditOrderBanner();

        // Fetch existing order to edit from date and order number input
        LocalDate date = view.getDateInput();
        int orderNumber = view.getOrderNumberInput();
        Order fetchedOrder = service.getOrder(date, orderNumber);

        if (fetchedOrder.getCustomerName() == null) {
            view.displayErrorMessage("Could not find order from information provided. Please try again.");
        }
        else {
            String customerName = fetchedOrder.getCustomerName();
            String state = fetchedOrder.getState();
            String productType = fetchedOrder.getProductType();
            BigDecimal area = fetchedOrder.getArea();

            List<Tax> taxes = service.getTaxes();
            List<Product> products = service.getProducts();

            Order order = view.getEditOrderInput(fetchedOrder, taxes, products);

            // Only edit or recalculate order if customer name, state, product type or area changes
            if (!order.getCustomerName().equals(customerName)
            || !order.getState().equals(state)
            || !order.getProductType().equals(productType)
            || !order.getArea().equals(area)) {
                this.calculateOrder(order);

                Order editedOrder = service.editOrder(date, orderNumber, order);

                if (editedOrder != null) {
                    view.displayEditOrderSuccess();
                }
            }
        }
    }

    private void removeOrder() throws PersistenceException, NoSuchOrderException {
        view.displayRemoveOrderBanner();

        LocalDate date = view.getDateInput();
        int orderNumber = view.getOrderNumberInput();

        // fetch order to remove from date and order number input
        Order fetchedOrder = service.getOrder(date, orderNumber);

        if (fetchedOrder == null || fetchedOrder.getCustomerName() == null) {
            view.displayErrorMessage("Could not find order from information provided. Please try again.");
        }
        else {
            boolean confirmation = view.getRemoveOrderConfirmation();

            if (confirmation) {
                Order removedOrder = service.removeOrder(date, orderNumber);

                if (removedOrder != null) {
                    view.displayRemoveOrderSuccess();
                }
            }
        }
    }

    // export data and all orders to external file
    private void exportData() throws PersistenceException {
        service.exportData();
        view.displayExportDataSuccess();
    }

    private void exitMessage() {
        view.displayExitMessage();
    }

    private void unknownCommand() {
        view.displayUnknownCommandMessage();
    }

    private void calculateOrder(Order order) {
        // Calculate material cost = area * cost per square foot
        BigDecimal area = order.getArea();
        BigDecimal costPerSquareFoot = order.getCostPerSquareFoot();
        BigDecimal materialCost = area.multiply(costPerSquareFoot);
        materialCost = materialCost.setScale(2, RoundingMode.HALF_EVEN);
        order.setMaterialCost(materialCost);

        // calculate labor cost = area * labor cost per square foot
        BigDecimal laborCostPerSquareFoot = order.getLaborCostPerSquareFoot();
        BigDecimal laborCost = area.multiply(laborCostPerSquareFoot);
        laborCost = laborCost.setScale(2, RoundingMode.HALF_EVEN);
        order.setLaborCost(laborCost);

        // calculate tax = (material cost + labor cost) * (tax rate / 100)
        BigDecimal taxRate = order.getTaxRate();
        BigDecimal tax = materialCost.add(laborCost).multiply(taxRate.divide(new BigDecimal(100)));
        tax = tax.setScale(2, RoundingMode.HALF_EVEN);
        order.setTax(tax);

        // calculate total = material cost + labor cost + tax
        BigDecimal total = materialCost.add(laborCost).add(tax);
        total = total.setScale(2, RoundingMode.HALF_EVEN);
        order.setTotal(total);
    }
}
