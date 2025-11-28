package com.letsteamup.service;

import com.letsteamup.model.Participant;
import com.letsteamup.util.ConsoleUI;
import com.letsteamup.exception.InvalidScoreException;
import com.letsteamup.exception.InvalidInputException;
import com.letsteamup.validator.InputValidator;
import com.letsteamup.LetsTeamUpApplication;

import java.util.*;

public class SurveyService {

    private Scanner scanner;
    private static final String[] PERSONALITY_QUESTIONS = {
            "I enjoy taking the lead and guiding others during group activities",
            "I prefer analyzing situations and coming up with strategic solutions",
            "I work well with others and enjoy collaborative teamwork",
            "I am calm under pressure and can help maintain team morale",
            "I like making quick decisions and adapting in dynamic situations"
    };

    private static final String[] AVAILABLE_GAMES = {
            "Valorant", "DOTA 2", "FIFA", "CS:GO", "Chess",
            "Basketball", "Badminton", "Football", "Cricket", "Table Tennis"
    };

    private static final String[] AVAILABLE_ROLES = {
            "Strategist", "Attacker", "Defender", "Supporter", "Coordinator"
    };

    public SurveyService() {
        this.scanner = new Scanner(System.in);
    }
    // Conducts the full interactive survey flow for a single participant,
    // collecting all inputs, validating them, and returning a Participant object.
    public Participant conductInteractiveSurvey() throws InvalidScoreException {
        try {
            String id = generateParticipantId();
            String name = getParticipantName();
            int age = getParticipantAge();
            String email = getParticipantEmail();

            int personalityScore = conductPersonalityAssessment();

            String preferredGame = selectGame();
            String preferredRole = selectRole();
            int skillLevel = getSkillLevel();

            LetsTeamUpApplication.logMessage("Survey completed for: " + name + " (Score: " + personalityScore + ")");

            return new Participant(id, name, age, email, personalityScore,
                    preferredGame, preferredRole, skillLevel);
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Validation error: " + e.getMessage());
            LetsTeamUpApplication.logMessage("Survey validation failed: " + e.getMessage());
            throw new InvalidScoreException("Survey validation failed", e);
        }
    }

    private String generateParticipantId() {
        return "P" + String.format("%03d", new Random().nextInt(1000));
    }
    // Continuously prompts the user for a valid participant name,
    // validating input and retrying until a correct name is entered.
    private String getParticipantName() throws InvalidInputException {
        while (true) {
            System.out.print("Enter participant name: ");
            String name = scanner.nextLine().trim();
            try {
                InputValidator.validateName(name);
                return name;
            } catch (InvalidInputException e) {
                ConsoleUI.printError(e.getMessage());
            }
        }
    }
    // Retrieves the participant's age by prompting the user and ensuring
    // the input falls within the valid range of 16 to 100.
    private int getParticipantAge() throws InvalidInputException {
        System.out.print("Enter age (16-100): ");
        return ConsoleUI.getIntInput(16, 100);
    }
    // Prompts the user for a valid email address, validating the format
    // and retrying until a correctly formatted email is entered.
    private String getParticipantEmail() throws InvalidInputException {
        while (true) {
            System.out.print("Enter email address: ");
            String email = scanner.nextLine().trim();
            try {
                InputValidator.validateEmail(email);
                return email;
            } catch (InvalidInputException e) {
                ConsoleUI.printError(e.getMessage());
            }
        }
    }
    // Conducts a multi-question personality assessment, collecting ratings,
    // calculating a scaled score, and determining the participant’s personality type.
    private int conductPersonalityAssessment() {
        ConsoleUI.printSeparator();
        System.out.println("PERSONALITY ASSESSMENT");
        System.out.println("Rate each statement (1=Strongly Disagree, 5=Strongly Agree)");
        ConsoleUI.printSeparator();

        int totalScore = 0;

        for (int i = 0; i < PERSONALITY_QUESTIONS.length; i++) {
            System.out.println("\nQuestion " + (i + 1) + ": " + PERSONALITY_QUESTIONS[i]);
            System.out.print("Your rating (1-5): ");
            int rating = ConsoleUI.getIntInput(1, 5);
            totalScore += rating;
        }

        int scaledScore = totalScore * 4;

        System.out.println("\nPersonality Score: " + scaledScore + "/100");
        System.out.println("Personality Type: " + classifyPersonality(scaledScore));

        return scaledScore;
    }
    // Classifies a participant’s personality type based on their scaled score,
    // returning one of the predefined categories such as Leader, Balanced, or Thinker.
    private String classifyPersonality(int score) {
        if (score >= 90) return "Leader";
        if (score >= 70) return "Balanced";
        if (score >= 50) return "Thinker";
        return "Unknown";
    }
    // Displays a list of available games and prompts the user to select one,
    // returning the chosen game after validating the input selection.
    private String selectGame() {
        ConsoleUI.printSeparator();
        System.out.println("GAME/SPORT SELECTION");
        System.out.println("Select your preferred game or sport:");
        ConsoleUI.printSeparator();

        for (int i = 0; i < AVAILABLE_GAMES.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), AVAILABLE_GAMES[i]);
        }

        System.out.print("\nEnter game number: ");
        int choice = ConsoleUI.getIntInput(1, AVAILABLE_GAMES.length);

        String selectedGame = AVAILABLE_GAMES[choice - 1];
        System.out.println("Selected game: " + selectedGame);

        return selectedGame;
    }
    // Presents a list of available roles for the participant to choose from,
    // validating the input and returning the selected role.
    private String selectRole() {
        ConsoleUI.printSeparator();
        System.out.println("ROLE SELECTION");
        System.out.println("Select your preferred playing role:");
        ConsoleUI.printSeparator();

        for (int i = 0; i < AVAILABLE_ROLES.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), AVAILABLE_ROLES[i]);
        }

        System.out.print("\nEnter role number: ");
        int choice = ConsoleUI.getIntInput(1, AVAILABLE_ROLES.length);

        String selectedRole = AVAILABLE_ROLES[choice - 1];
        System.out.println("Selected role: " + selectedRole);

        return selectedRole;
    }
    // Prompts the participant to rate their skill level from 1–10 and validates
    // the input to ensure it falls within the defined experience range.
    private int getSkillLevel() {
        ConsoleUI.printSeparator();
        System.out.println("SKILL LEVEL ASSESSMENT");
        System.out.println("Rate your overall gaming/sports skill level (1-10)");
        System.out.println("1-3: Beginner | 4-6: Intermediate | 7-9: Advanced | 10: Expert");
        ConsoleUI.printSeparator();

        System.out.print("Your skill level: ");
        return ConsoleUI.getIntInput(1, 10);
    }
}