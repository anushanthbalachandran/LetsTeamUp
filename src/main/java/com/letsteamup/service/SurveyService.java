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

    private int getParticipantAge() throws InvalidInputException {
        System.out.print("Enter age (16-100): ");
        return ConsoleUI.getIntInput(16, 100);
    }

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

    private String classifyPersonality(int score) {
        if (score >= 90) return "Leader";
        if (score >= 70) return "Balanced";
        if (score >= 50) return "Thinker";
        return "Unknown";
    }

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