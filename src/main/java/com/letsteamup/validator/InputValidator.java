package com.letsteamup.validator;

import com.letsteamup.exception.InvalidInputException;
import java.util.regex.Pattern;

/**
 * Comprehensive input validation utility
 * Handles all input validation with detailed error messages
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * Validates email format
     */
    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
    }

    /**
     * Validates name format
     */
    public static void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        if (name.length() < 2) {
            throw new InvalidInputException("Name must be at least 2 characters long");
        }

        if (name.length() > 100) {
            throw new InvalidInputException("Name must be less than 100 characters");
        }
    }

    /**
     * Validates age range
     */
    public static void validateAge(int age) throws InvalidInputException {
        if (age < 16 || age > 100) {
            throw new InvalidInputException("Age must be between 16 and 100");
        }
    }

    /**
     * Validates skill level
     */
    public static void validateSkillLevel(int skillLevel) throws InvalidInputException {
        if (skillLevel < 1 || skillLevel > 10) {
            throw new InvalidInputException("Skill level must be between 1 and 10");
        }
    }

    /**
     * Validates personality score
     */
    public static void validatePersonalityScore(int score) throws InvalidInputException {
        if (score < 0 || score > 100) {
            throw new InvalidInputException("Personality score must be between 0 and 100. Received: " + score);
        }
    }

    /**
     * Validates role
     */
    public static void validateRole(String role) throws InvalidInputException {
        if (role == null || role.trim().isEmpty()) {
            throw new InvalidInputException("Role cannot be empty");
        }

        String[] validRoles = {"Strategist", "Attacker", "Defender", "Supporter", "Coordinator"};
        boolean isValid = false;
        for (String validRole : validRoles) {
            if (validRole.equalsIgnoreCase(role)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new InvalidInputException("Invalid role: " + role +
                    ". Valid roles are: Strategist, Attacker, Defender, Supporter, Coordinator");
        }
    }

    /**
     * Validates game name
     */
    public static void validateGame(String game) throws InvalidInputException {
        if (game == null || game.trim().isEmpty()) {
            throw new InvalidInputException("Game/Sport cannot be empty");
        }

        if (game.length() < 2) {
            throw new InvalidInputException("Game/Sport name must be at least 2 characters");
        }
    }

    /**
     * Validates participant ID format
     */
    public static void validateId(String id) throws InvalidInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("ID cannot be empty");
        }

        if (!id.matches("^P\\d+$")) {
            throw new InvalidInputException("ID must start with 'P' followed by numbers (e.g., P001)");
        }
    }

    /**
     * Validates team size
     */
    public static void validateTeamSize(int teamSize, int participantCount) throws InvalidInputException {
        if (teamSize < 2) {
            throw new InvalidInputException("Team size must be at least 2");
        }

        if (teamSize > participantCount) {
            throw new InvalidInputException("Team size cannot exceed total participants");
        }
    }

    /**
     * Validates file path
     */
    public static void validateFilePath(String filePath) throws InvalidInputException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InvalidInputException("File path cannot be empty");
        }

        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new InvalidInputException("File must be a CSV file (.csv extension)");
        }
    }

    /**
     * Validates integer input in range
     */
    public static void validateIntegerRange(int value, int min, int max, String fieldName)
            throws InvalidInputException {
        if (value < min || value > max) {
            throw new InvalidInputException(fieldName + " must be between " + min + " and " + max);
        }
    }

    /**
     * Validates string is not empty
     */
    public static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }
}