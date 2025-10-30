package com.sg.flooringmastery.exceptions;

public class NoSuchOrderException extends Exception {
    public NoSuchOrderException(String message) {
        super(message);
    }

    public NoSuchOrderException(String message, Throwable cause) {
        super(cause);
    }
}
