package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;

public interface AuditFileDaoInterface {
    void writeAuditEntry(String entry)
            throws PersistenceException;
}
