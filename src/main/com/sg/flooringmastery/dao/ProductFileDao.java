package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class ProductFileDao implements ProductFileDaoInterface {
    private final Map<String, Product> allProducts = new HashMap<>();
    private static final String PRODUCT_FILE = "taxes.txt";
    private static final String DELIMITER = ",";

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        loadFile();
        return allProducts.values().stream().toList();
    }

    private Product unmarshallProduct(String productString) {
        String[] productTokens = productString.split(DELIMITER);
        Product product = new Product();
        product.setProductType(productTokens[0]);
        product.setCostPerSquareFoot(BigDecimal.valueOf(Integer.parseInt(productTokens[1])));
        product.setLaborCostPerSquareFoot(BigDecimal.valueOf(Integer.parseInt(productTokens[2])));
        return product;
    }

    private void loadFile() throws PersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Error: File not found.");
        }

        String currentLine;
        Product currentProduct;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            allProducts.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
    }
}
