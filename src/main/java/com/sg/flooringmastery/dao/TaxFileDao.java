package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class TaxFileDao implements TaxFileDaoInterface {
    private Map<String, Tax> allTaxes = new HashMap<>();
    private static final String TAX_FILE = "data/taxes.txt";
    private static final String DELIMITER = ",";

    @Override
    public List<Tax> getAllTaxes() throws PersistenceException {
        loadFile();
        return allTaxes.values().stream().toList();
    }

    private Tax unmarshallTax(String taxString) {
        String[] taxTokens = taxString.split(DELIMITER);
        Tax tax = new Tax();
        tax.setStateAbbreviation(taxTokens[0]);
        tax.setStateName(taxTokens[1]);
        tax.setTaxRate(BigDecimal.valueOf(Double.parseDouble(taxTokens[2])));
        return tax;
    }

    private void loadFile() throws PersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Error: could not find file.");
        }

        String currentLine;
        Tax currentTax;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);
            allTaxes.put(currentTax.getStateAbbreviation(), currentTax);
        }

        scanner.close();
    }
}
