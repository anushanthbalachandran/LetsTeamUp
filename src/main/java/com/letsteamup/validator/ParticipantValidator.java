package com.letsteamup.validator;

import com.letsteamup.exception.InvalidScoreException;
import com.letsteamup.exception.InvalidRoleException;

import java.util.Arrays;
import java.util.List;

/**
 * Validator class for participant data
 * Ensures data integrity and business rule compliance
 */
public class ParticipantValidator {

    private static final List<String> VALID_ROLES = Arrays.asList(
            "Strategist", "Attacker", "Defender", "Supporter", "Coordinator"
    );

    private static final int MIN_AGE = 16;
    private static final int MAX_AGE = 100;

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;

    private static final int MIN_SKILL = 1;
    private static final int MAX_SKILL = 10;

    /**
     * Validates personality score
     */
    public static void validatePersonalityScore(int score) throws InvalidScoreException {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new InvalidScoreException(
                    "Personality score must be between " + MIN_SCORE + " and " + MAX_SCORE +
                            ". Received: " + score
            );
        }
    }

    /**
     * Validates role
     */
    public static void validateRole(String role) throws InvalidRoleException {
        if (role == null || role.trim().isEmpty()) {
            throw new InvalidRoleException("Role cannot be empty");
        }

        if (!VALID_ROLES.contains(role)) {
            throw new InvalidRoleException(
                    "Invalid role: " + role + ". Valid roles are: " + String.join(", ", VALID_ROLES)
            );
        }
    }

    /**
     * Validates age
     */
    public static void validateAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                    "Age must be between " + MIN_AGE + " and " + MAX_AGE
            );
        }
    }

    /**
     * Validates skill level
     */
    public static void validateSkillLevel(int skillLevel) {
        if (skillLevel < MIN_SKILL || skillLevel > MAX_SKILL) {
            throw new IllegalArgumentException(
                    "Skill level must be between " + MIN_SKILL + " and " + MAX_SKILL
            );
        }
    }

    /**
     * Validates email format
     */
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    /**
     * Validates name
     */
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (name.length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }
    }

    /**
     * Validates game name
     */
    public static void validateGame(String game) {
        if (game == null || game.trim().isEmpty()) {
            throw new IllegalArgumentException("Game/Sport cannot be empty");
        }

        if (game.length() < 2) {
            throw new IllegalArgumentException("Game/Sport name must be at least 2 characters long");
        }
    }

    /**
     * Gets list of valid roles
     */
    public static List<String> getValidRoles() {
        return VALID_ROLES;
    }
}