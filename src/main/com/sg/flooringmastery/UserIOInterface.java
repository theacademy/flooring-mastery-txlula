package com.sg.flooringmastery;

import java.math.BigDecimal;

public interface UserIOInterface {
    void printEmptyLine();
    void printNextLine(String message);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
}
