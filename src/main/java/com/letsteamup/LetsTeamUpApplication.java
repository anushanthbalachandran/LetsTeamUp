package com.letsteamup;

import com.letsteamup.controller.MenuController;
import com.letsteamup.exception.FileProcessingException;
import com.letsteamup.exception.InsufficientParticipantsException;
import com.letsteamup.exception.InvalidScoreException;
import com.letsteamup.service.DataService;
import com.letsteamup.service.SurveyService;
import com.letsteamup.service.TeamFormationService;
import com.letsteamup.util.ConsoleUI;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LetsTeamUpApplication {

    private static Scanner scanner = new Scanner(System.in);
    private static MenuController menuController;
    private static BufferedWriter logWriter;

    public static void main(String[] args) {
        initializeLogger();

        logMessage("Application started");
        ConsoleUI.printHeader("TeamMate: Intelligent Team Formation System");

        DataService dataService = new DataService();
        SurveyService surveyService = new SurveyService();
        TeamFormationService teamFormationService = new TeamFormationService();

        menuController = new MenuController(dataService, surveyService, teamFormationService);

        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getMenuChoice(1, 7);

            try {
                switch (choice) {
                    case 1:
                        logMessage("User selected: Conduct Survey");
                        menuController.conductSurvey();
                        break;
                    case 2:
                        logMessage("User selected: Load Participants");
                        menuController.loadParticipantsFromFile();
                        break;
                    case 3:
                        logMessage("User selected: View Participants");
                        menuController.viewParticipants();
                        break;
                    case 4:
                        logMessage("User selected: Form Teams");
                        menuController.formTeams();
                        break;
                    case 5:
                        logMessage("User selected: View Teams");
                        menuController.viewFormedTeams();
                        break;
                    case 6:
                        logMessage("User selected: Export Teams");
                        menuController.exportTeamsToFile();
                        break;
                    case 7:
                        running = false;
                        logMessage("Application exit requested");
                        ConsoleUI.printSuccess("Thank you for using TeamMate!");
                        break;
                }
            } catch (InvalidScoreException | FileProcessingException | InsufficientParticipantsException e) {
                ConsoleUI.printError("Operation failed: " + e.getMessage());
                logMessage("ERROR: " + e.getMessage());
            } catch (Exception e) {
                ConsoleUI.printError("Unexpected error: " + e.getMessage());
                logMessage("CRITICAL ERROR: " + e.getMessage());
                e.printStackTrace();
            }

            if (running && choice != 7) {
                ConsoleUI.pressEnterToContinue();
            }
        }

        scanner.close();
        menuController.cleanup();
        closeLogger();
    }

    private static void displayMainMenu() {
        ConsoleUI.printSeparator();
        System.out.println("MAIN MENU");
        ConsoleUI.printSeparator();
        System.out.println("1. Conduct Participant Survey");
        System.out.println("2. Load Participants from CSV");
        System.out.println("3. View All Participants");
        System.out.println("4. Form Teams");
        System.out.println("5. View Formed Teams");
        System.out.println("6. Export Teams to CSV");
        System.out.println("7. Exit");
        ConsoleUI.printSeparator();
    }

    private static int getMenuChoice(int min, int max) {
        while (true) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void initializeLogger() {
        try {
            String resourcesPath = "src/main/resources/logs";
            File logDir = new File(resourcesPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File logFile = new File(logDir, "application_" + timestamp + ".log");

            logWriter = new BufferedWriter(new FileWriter(logFile, true));
            logMessage("Logger initialized at: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static void logMessage(String message) {
        if (logWriter != null) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                logWriter.write("[" + timestamp + "] " + message);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                System.err.println("Failed to write log: " + e.getMessage());
            }
        }
    }

    private static void closeLogger() {
        if (logWriter != null) {
            try {
                logMessage("Application terminated");
                logWriter.close();
            } catch (IOException e) {
                System.err.println("Failed to close logger: " + e.getMessage());
            }
        }
    }
}