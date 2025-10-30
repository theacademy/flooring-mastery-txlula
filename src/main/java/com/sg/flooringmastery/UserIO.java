package com.sg.flooringmastery;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserIO implements UserIOInterface {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void printEmptyLine() {
        System.out.println();
    }

    @Override
    public void printNextLine(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        boolean invalidInput = true;
        int num = 0;

        while (invalidInput) {
            try {
                String value = readString(prompt);
                num = Integer.parseInt(value);
                invalidInput = false;
            }
            catch (NumberFormatException e) {
                this.printNextLine("Input error. Please try again.");
            }
        }

        return num;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int num;

        do {
            num = readInt(prompt);
        } while (num < min || num > max);

        return num;
    };
}
