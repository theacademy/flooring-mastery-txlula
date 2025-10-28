package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import com.sg.flooringmastery.model.Product;

import java.util.List;

public interface ProductFileDaoInterface {
    List<Product> getAllProducts()
        throws PersistenceException;
}
