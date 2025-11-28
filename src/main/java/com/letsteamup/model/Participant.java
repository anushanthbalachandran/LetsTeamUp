package com.letsteamup.model;

import java.util.List;

/**
 * Represents a participant in the gaming club
 * Implements encapsulation through private attributes and public accessors
 */
public class Participant {
    private String id;
    private String name;
    private int age;
    private String email;
    private int personalityScore;
    private String personalityType;
    private String preferredGame;
    private String preferredRole;
    private int skillLevel;

    // Default constructor
    public Participant() {
    }

    // Parameterized constructor
    public Participant(String id, String name, int age, String email,
                       int personalityScore, String preferredGame,
                       String preferredRole, int skillLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.personalityScore = personalityScore;
        this.preferredGame = preferredGame;
        this.preferredRole = preferredRole;
        this.skillLevel = skillLevel;
        this.personalityType = classifyPersonality(personalityScore);
    }

    /**
     * Classifies personality based on score
     * Leader: 90-100, Balanced: 70-89, Thinker: 50-69
     */
    private String classifyPersonality(int score) {
        if (score >= 90 && score <= 100) {
            return "Leader";
        } else if (score >= 70 && score < 90) {
            return "Balanced";
        } else if (score >= 50 && score < 70) {
            return "Thinker";
        } else {
            return "Unknown";
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPersonalityScore() {
        return personalityScore;
    }

    public void setPersonalityScore(int personalityScore) {
        this.personalityScore = personalityScore;
        this.personalityType = classifyPersonality(personalityScore);
    }

    public String getPersonalityType() {
        return personalityType;
    }

    public String getPreferredGame() {
        return preferredGame;
    }

    public void setPreferredGame(String preferredGame) {
        this.preferredGame = preferredGame;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", personalityType='" + personalityType + '\'' +
                ", role='" + preferredRole + '\'' +
                ", game='" + preferredGame + '\'' +
                '}';
    }
}