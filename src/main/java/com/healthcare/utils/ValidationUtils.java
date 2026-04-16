package com.healthcare.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for input validation and formatting.
 */
public class ValidationUtils {

    /**
     * Gets a validated integer within a specified range.
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @param min     minimum allowed value
     * @param max     maximum allowed value
     * @return the validated integer
     */
    public static int getValidatedInteger(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }

    /**
     * Gets a validated email address.
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @return the validated email (lowercase)
     */
    public static String getValidatedEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim().toLowerCase();
            int atIndex = email.indexOf('@');
            int lastAt = email.lastIndexOf('@');
            if (email.isEmpty() || atIndex <= 0 || atIndex != lastAt || atIndex == email.length() - 1) {
                System.out.println("Invalid email. Please include a single '@' and a valid domain.");
                continue;
            }
            if (email.indexOf('.', atIndex) == -1) {
                System.out.println("Invalid email. Please include a '.' after the '@' sign.");
                continue;
            }
            return email;
        }
    }

    /**
     * Gets a validated name (letters only).
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @return the validated name (capitalized)
     */
    public static String getValidatedName(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = scanner.nextLine().trim();
            if (name.isEmpty() || !name.matches("[A-Za-z]+")) {
                System.out.println("Invalid name. Please use only letters with no numbers or symbols.");
                continue;
            }
            return capitalizeName(name);
        }
    }

    /**
     * Capitalizes a name (first letter uppercase, rest lowercase).
     *
     * @param name the name to capitalize
     * @return the capitalized name
     */
    public static String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * Gets a validated phone number (exactly 10 digits).
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @return the validated phone number
     */
    public static String getValidatedPhone(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = scanner.nextLine().trim();
            if (!phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
                continue;
            }
            return phone;
        }
    }

    /**
     * Gets a validated non-empty string.
     *
     * @param scanner      scanner for keyboard input
     * @param prompt       the prompt to display
     * @param errorMessage error message if empty
     * @return the validated non-empty string
     */
    public static String getValidatedNonEmptyString(Scanner scanner, String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(errorMessage);
                continue;
            }
            return input;
        }
    }

    /**
     * Gets a validated date in ISO format (YYYY-MM-DD), cannot be in the past.
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @return the validated LocalDate
     */
    public static LocalDate getValidatedDate(Scanner scanner, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                LocalDate earliest = LocalDate.now();
                if (date.isBefore(earliest)) {
                    System.out.println("Date cannot be in the past. Please enter " + earliest + " or later.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD and enter a real date.");
            }
        }
    }

    /**
     * Gets a validated severity level (Low/Medium/High).
     *
     * @param scanner scanner for keyboard input
     * @param prompt  the prompt to display
     * @return the validated severity
     */
    public static String getValidatedSeverity(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("low")) {
                return "Low";
            }
            if (input.equalsIgnoreCase("medium")) {
                return "Medium";
            }
            if (input.equalsIgnoreCase("high")) {
                return "High";
            }
            System.out.println("Invalid severity. Please enter Low, Medium, or High.");
        }
    }
}
