package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Tax;

import java.util.List;

public interface TaxFileDaoInterface {
    List<Tax> getAllTaxes()
            throws PersistenceException;
}