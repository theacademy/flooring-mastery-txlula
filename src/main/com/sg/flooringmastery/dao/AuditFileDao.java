package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.exceptions.PersistenceException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class AuditFileDao implements AuditFileDaoInterface {
    private static final String AUDIT_FILE = "audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws PersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        }
        catch (IOException e) {
            throw new PersistenceException("Error: could not write to audit file.", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp +  " : " + entry);
        out.flush();
    }
}
