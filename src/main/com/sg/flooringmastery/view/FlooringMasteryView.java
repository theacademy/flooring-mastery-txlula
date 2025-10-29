package com.sg.flooringmastery.view;

import com.sg.flooringmastery.UserIO;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class FlooringMasteryView {
    private final UserIO io;

    @Autowired
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int displayMenuAndGetSelection() {
        io.printNextLine("*** Flooring Program ***");
        io.printNextLine("1. Display Orders");
        io.printNextLine("2. Add an Order");
        io.printNextLine("3. Edit an Order");
        io.printNextLine("4. Remove an Order");
        io.printNextLine("5. Export All Data");
        io.printNextLine("6. Quit");
        io.printEmptyLine();

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public LocalDate getDateInput() {
        io.printEmptyLine();
        boolean invalidInput = true;
        LocalDate date = LocalDate.now();
        while (invalidInput) {
            String value = io.readString("Please enter order date in dd-MM-yyyy format.");

            try {
                date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                if (date.isBefore(LocalDate.now().plusDays(1))) {
                    invalidInput = false;
                }
                else {
                    io.printEmptyLine();
                    io.printNextLine("Error: Date is in the future. Please try again.");
                }
            }
            catch (Exception e) {
                io.printEmptyLine();
                io.printNextLine("Error: Date is invalid.");
            }
        }
        return date;
    }

    public void displayNoOrders() {
        io.printEmptyLine();
        io.printNextLine("No orders found.");
        io.printEmptyLine();
    }

    public void displayOrders(LocalDate date, List<Order> orders) {
        io.printEmptyLine();
        io.printNextLine("*** " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " ***");
        io.printEmptyLine();

        for (Order order: orders) {
            io.printNextLine("#" + order.getOrderNumber());
            io.printNextLine("Customer Name: " + order.getCustomerName());
            io.printNextLine("State: " + order.getState());
            io.printNextLine("Tax Rate: " + order.getTaxRate());
            io.printNextLine("Product Type: " + order.getProductType());
            io.printNextLine("Area: " + order.getArea().toString());
            io.printNextLine("Cost Per Square Foot: " + order.getCostPerSquareFoot().toString());
            io.printNextLine("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot().toString());
            io.printNextLine("Material Cost: " + order.getMaterialCost());
            io.printNextLine("Labor Cost: " + order.getLaborCost());
            io.printNextLine("Tax: " + order.getTax().toString());
            io.printNextLine("Total: " + order.getTotal().toString());
            io.printEmptyLine();
        }

        io.readString("End of list. Press enter to return to main menu.");
    }

    public void displayOrderInfo(Order order) {
        io.printNextLine("#" + order.getOrderNumber());
        io.printNextLine("Customer Name: " + order.getCustomerName());
        io.printNextLine("State: " + order.getState());
        io.printNextLine("Tax Rate: " + order.getTaxRate());
        io.printNextLine("Product Type: " + order.getProductType());
        io.printNextLine("Area: " + order.getArea().toString());
        io.printNextLine("Cost Per Square Foot: " + order.getCostPerSquareFoot().toString());
        io.printNextLine("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot().toString());
        io.printNextLine("Material Cost: " + order.getMaterialCost());
        io.printNextLine("Labor Cost: " + order.getLaborCost());
        io.printNextLine("Tax: " + order.getTax().toString());
        io.printNextLine("Total: " + order.getTotal().toString());
        io.printEmptyLine();
    }

    public void displayAddOrderBanner() {
        io.printEmptyLine();
        io.printNextLine("*** Add Order ***");
        io.printEmptyLine();
    }

    public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
        Order order = new Order();

        boolean hasErrors = true;

        while (hasErrors) {
            String customerName = io.readString("Please enter customer name.");
            customerName = this.validateNameInput(customerName);

            if (!customerName.isEmpty()) {
                order.setCustomerName(customerName);
                hasErrors = false;
            }
        }

        io.printEmptyLine();
        hasErrors = true;
        while (hasErrors) {
            String stateName = io.readString("Please enter state name for tax.");
            Tax tax = this.validateStateInput(stateName, taxes);

            if (tax != null) {
                order.setTaxRate(tax.getTaxRate());
                order.setState(tax.getStateName());
                hasErrors = false;
            }
        }

        io.printEmptyLine();
        hasErrors = true;
        while (hasErrors) {
            io.printNextLine("*** Products ***");
            for (Product listedProduct : products) {
                io.printNextLine(listedProduct.getProductType());
                io.printNextLine("Cost Per Square Foot: " + listedProduct.getCostPerSquareFoot());
                io.printNextLine("Labor Cost Per Square Foot: " + listedProduct.getLaborCostPerSquareFoot());
                io.printEmptyLine();
            }

            String productType = io.readString("Please enter product type.");
            Product product = this.validateProductInput(productType, products);

            if (product != null) {
                order.setProductType(product.getProductType());
                order.setCostPerSquareFoot(product.getCostPerSquareFoot());
                order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
                hasErrors = false;
            }
        }

        io.printEmptyLine();
        hasErrors = true;
        while (hasErrors) {
            String areaInput = io.readString("Please enter area.");
            BigDecimal area = this.validateAreaInput(areaInput);

            if (area != null) {
                order.setArea(area);
                hasErrors = false;
            }
        }

        return order;
    }

    public void displayAddOrderSuccess() {
        io.printEmptyLine();
        io.printNextLine("Successfully added order!");
        io.readString("Please enter to return to main menu.");
    }

    public void displayEditOrderBanner() {
        io.printEmptyLine();
        io.printNextLine("*** Edit Order ***");
        io.printEmptyLine();
    }

    public int getOrderNumberInput() {
        io.printEmptyLine();
        return io.readInt("Please enter order number.");
    }

    public Order getEditOrderInput(Order order, List<Tax> taxes, List<Product> products) {
        io.printEmptyLine();
        this.displayOrderInfo(order);

        boolean hasErrors = true;

        while (hasErrors) {
            String customerName = io.readString("Please enter customer name. (" + order.getCustomerName() + ")");

            if (customerName.isEmpty()) {
                hasErrors = false;
            }
            else {
                customerName = this.validateNameInput(customerName);

                if (!customerName.isEmpty()) {
                    order.setCustomerName(customerName);
                    hasErrors = false;
                }
            }
        }

        hasErrors = true;
        while (hasErrors) {
            String stateName = io.readString("Please enter state name for tax. (" + order.getState() + ")");

            if (stateName.isEmpty()) {
                hasErrors = false;
            }
            else {
                Tax tax = this.validateStateInput(stateName, taxes);

                if (tax != null) {
                    order.setTaxRate(tax.getTaxRate());
                    order.setState(tax.getStateName());
                    hasErrors = false;
                }
            }
        }

        hasErrors = true;
        while (hasErrors) {
            for (Product listedProduct : products) {
                io.printNextLine(listedProduct.getProductType());
                io.printNextLine("Cost Per Square Foot: " + listedProduct.getCostPerSquareFoot());
                io.printNextLine("Labor Cost Per Square Foot: " + listedProduct.getLaborCostPerSquareFoot());
                io.printEmptyLine();
            }

            String productType = io.readString("Please enter product type. (" + order.getProductType() + ")");

            if (productType.isEmpty()) {
                hasErrors = false;
            }
            else {
                Product product = this.validateProductInput(productType, products);

                if (product != null) {
                    order.setProductType(product.getProductType());
                    order.setCostPerSquareFoot(product.getCostPerSquareFoot());
                    order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
                    hasErrors = false;
                }
            }
        }

        hasErrors = true;
        while (hasErrors) {
            String areaInput = io.readString("Please enter area. (" + order.getArea() + ")");

            if (areaInput.isEmpty()) {
                hasErrors = false;
            }
            else {
                BigDecimal area = this.validateAreaInput(areaInput);

                if (area != null) {
                    order.setArea(area);
                    hasErrors = false;
                }
            }
        }

        return order;
    }

    public void displayEditOrderSuccess() {
        io.printEmptyLine();
        io.printNextLine("Successfully edited order!");
        io.readString("Please enter to return to main menu.");
    }

    public void displayRemoveOrderBanner() {
        io.printEmptyLine();
        io.printNextLine("*** Remove Order ***");
        io.printEmptyLine();
    }

    public boolean getAddOrderConfirmation() {
        io.printEmptyLine();
        String confirm = io.readString("Do you want to place the order? (y/n)");
        return confirm.equals("y");
    }

    public boolean getRemoveOrderConfirmation() {
        io.printEmptyLine();
        String confirm = io.readString("Do you want to remove the order? (y/n)");
        return confirm.equals("y");
    }

    public void displayRemoveOrderSuccess() {
        io.printEmptyLine();
        io.printNextLine("Successfully removed order!");
        io.printEmptyLine();
    }

    public void displayExportDataSuccess() {
        io.printEmptyLine();
        io.printNextLine("Successfully exported data!");
        io.readString("Please enter to return to main menu.");
    }

    public void displayExitMessage() {
        io.printEmptyLine();
        io.printNextLine("Exiting system...");
        io.printNextLine("Goodbye!");
    }

    public void displayErrorMessage(String errorMessage) {
        io.printEmptyLine();
        io.printNextLine(errorMessage);
        io.readString("Please enter to return to main menu.");
    }

    public void displayUnknownCommandMessage() {
        io.printEmptyLine();
        io.printNextLine("Unknown command. Try again.");
        io.readString("Please enter to return to main menu.");
    }

    private String validateNameInput(String customerName) {
        if (!Pattern.matches("[a-zA-Z0-9,.]+", customerName)) {
            io.printEmptyLine();
            io.printNextLine("Name must be alphanumeric and can include periods and comma characters. Please try again.");
            io.printEmptyLine();
            return "";
        }
        return customerName;
    }

    private Tax validateStateInput(String stateName, List<Tax> taxes) {
        List<Tax> fetchedTaxes = taxes.stream().filter(t -> t.getStateName().equals(stateName)).toList();

        if (fetchedTaxes.isEmpty()) {
            io.printEmptyLine();
            io.printNextLine("State name is invalid. Please try again.");
            io.printEmptyLine();
            return null;
        }
        else {
            return fetchedTaxes.get(0);
        }
    }

    private Product validateProductInput(String productType, List<Product> products) {
        List<Product> fetchedProducts = products.stream().filter(p -> p.getProductType().equals(productType)).toList();

        if (fetchedProducts.isEmpty()) {
            io.printEmptyLine();
            io.printNextLine("Product type is invalid. Please try again.");
            io.printEmptyLine();
            return null;
        }
        else {
            return fetchedProducts.get(0);
        }
    }

    private BigDecimal validateAreaInput(String areaInput) {
        if (!areaInput.matches("[0-9.]+")) {
            io.printEmptyLine();
            io.printNextLine("Area must be a number. Please try again.");
            io.printEmptyLine();
            return null;
        }

        BigDecimal area = new BigDecimal(Double.parseDouble(areaInput));

        if (area.compareTo(new BigDecimal(100)) < 0) {
            io.printEmptyLine();
            io.printNextLine("Area must be positive and at least 100 sq ft. Please try again.");
            io.printEmptyLine();
            return null;
        }

        return area;
    }
}
