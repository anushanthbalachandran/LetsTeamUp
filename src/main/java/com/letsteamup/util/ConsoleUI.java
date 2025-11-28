package com.letsteamup.util;

import java.util.Scanner;

/**
 * Utility class for console UI formatting and input handling
 */
public class ConsoleUI {

    private static final String SEPARATOR = "=================================================";
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Prints a formatted header
     */
    public static void printHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  " + title);
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a separator line
     */
    public static void printSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Prints success message
     */
    public static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    /**
     * Prints error message
     */
    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Prints info message
     */
    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    /**
     * Prints warning message
     */
    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    /**
     * Gets validated integer input within range
     */
    public static int getIntInput(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

                printError("Please enter a number between " + min + " and " + max);

            } catch (NumberFormatException e) {
                printError("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Gets string input with validation
     */
    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Gets yes/no confirmation
     */
    public static boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            printError("Please enter 'y' or 'n'");
        }
    }

    /**
     * Pauses execution until Enter is pressed
     */
    public static void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Clears console (works on most terminals)
     */
    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}