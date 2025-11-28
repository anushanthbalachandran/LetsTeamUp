package com.letsteamup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a team formed from participants
 * Demonstrates composition relationship with Participant class
 */
public class Team {
    private String teamId;
    private String teamName;
    private List<Participant> members;
    private int maxSize;

    // Constructor
    public Team(String teamId, int maxSize) {
        this.teamId = teamId;
        this.teamName = "Team " + teamId;
        this.members = new ArrayList<>();
        this.maxSize = maxSize;
    }

    /**
     * Adds a participant to the team
     * @param participant The participant to add
     * @return true if added successfully, false if team is full
     */
    public boolean addMember(Participant participant) {
        if (members.size() < maxSize) {
            members.add(participant);
            return true;
        }
        return false;
    }

    /**
     * Checks if team is full
     */
    public boolean isFull() {
        return members.size() >= maxSize;
    }

    /**
     * Gets diversity score based on different games
     */
    public int getDiversityScore() {
        return (int) members.stream()
                .map(Participant::getPreferredGame)
                .distinct()
                .count();
    }

    /**
     * Gets list of personality types in the team
     */
    public List<String> getPersonalityTypes() {
        return members.stream()
                .map(Participant::getPersonalityType)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets list of roles in the team
     */
    public List<String> getRoles() {
        return members.stream()
                .map(Participant::getPreferredRole)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets list of games in the team
     */
    public List<String> getGames() {
        return members.stream()
                .map(Participant::getPreferredGame)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Calculates average skill level of the team
     */
    public double getAverageSkillLevel() {
        return members.stream()
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(0.0);
    }

    // Getters and Setters
    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Participant> getMembers() {
        return members;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getCurrentSize() {
        return members.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(teamName).append(" (").append(members.size()).append("/").append(maxSize).append(")\n");
        for (Participant p : members) {
            sb.append("  - ").append(p.getName())
                    .append(" (").append(p.getPersonalityType())
                    .append(", ").append(p.getPreferredRole())
                    .append(", ").append(p.getPreferredGame())
                    .append(")\n");
        }
        return sb.toString();
    }
}