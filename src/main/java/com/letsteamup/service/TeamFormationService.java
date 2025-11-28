package com.letsteamup.service;

import com.letsteamup.model.Participant;
import com.letsteamup.model.Team;
import com.letsteamup.exception.InsufficientParticipantsException;
import com.letsteamup.LetsTeamUpApplication;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TeamFormationService {

    private List<Team> formedTeams;
    private ExecutorService executorService;
    // Initializes the team formation service by setting up storage for formed teams
    // and creating a thread pool optimized for parallel team-forming operations.
    public TeamFormationService() {
        this.formedTeams = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }
    // Creates balanced teams by distributing participants to maximize diversity,
    // validating inputs, forming each team, and returning the final team list.
    public List<Team> formBalancedTeams(List<Participant> participants, int teamSize)
            throws InsufficientParticipantsException {

        validateInput(participants, teamSize);

        formedTeams.clear();
        List<Participant> available = new ArrayList<>(participants);
        int numberOfTeams = participants.size() / teamSize;

        LetsTeamUpApplication.logMessage("Forming " + numberOfTeams + " balanced teams with size " + teamSize);

        for (int i = 0; i < numberOfTeams; i++) {
            Team team = createBalancedTeam("T" + (i + 1), available, teamSize);
            if (team.getCurrentSize() > 0) {
                formedTeams.add(team);
            }
        }

        LetsTeamUpApplication.logMessage("Successfully formed " + formedTeams.size() + " teams");
        return new ArrayList<>(formedTeams);
    }
    // Builds a balanced team by prioritizing personality diversity, limiting leaders,
    // and controlling game distribution before assigning members to the team.
    private Team createBalancedTeam(String teamId, List<Participant> available, int teamSize) {
        Team team = new Team(teamId, teamSize);

        Map<String, List<Participant>> byPersonality = available.stream()
                .collect(Collectors.groupingBy(Participant::getPersonalityType));

        List<Participant> selected = new ArrayList<>();
        Map<String, Integer> gameCount = new HashMap<>();
        int leaderCount = 0;

        List<Participant> leaders = byPersonality.getOrDefault("Leader", new ArrayList<>());
        if (!leaders.isEmpty() && leaderCount < 2) {
            Participant leader = leaders.get(0);
            selected.add(leader);
            leaderCount++;
            gameCount.put(leader.getPreferredGame(), 1);
        }

        for (Participant p : available) {
            if (selected.size() >= teamSize) break;
            if (selected.contains(p)) continue;

            int currentGameCount = gameCount.getOrDefault(p.getPreferredGame(), 0);

            if (p.getPersonalityType().equals("Leader")) {
                if (leaderCount >= 2) continue;
            }

            if (currentGameCount < 2) {
                selected.add(p);
                gameCount.put(p.getPreferredGame(), currentGameCount + 1);
                if (p.getPersonalityType().equals("Leader")) {
                    leaderCount++;
                }
            }
        }

        for (Participant p : available) {
            if (selected.size() >= teamSize) break;
            if (selected.contains(p)) continue;

            if (p.getPersonalityType().equals("Leader") && leaderCount >= 2) {
                continue;
            }

            selected.add(p);
            if (p.getPersonalityType().equals("Leader")) {
                leaderCount++;
            }
        }

        for (Participant p : selected) {
            team.addMember(p);
            available.remove(p);
        }

        return team;
    }
    // Forms teams by sorting participants by skill (highest to lowest) and distributing
    // them in a zigzag pattern to keep teams balanced while respecting leader limits.
    public List<Team> formSkillBasedTeams(List<Participant> participants, int teamSize)
            throws InsufficientParticipantsException {

        validateInput(participants, teamSize);

        formedTeams.clear();

        List<Participant> sorted = new ArrayList<>(participants);
        sorted.sort(Comparator.comparingInt(Participant::getSkillLevel).reversed());

        int numberOfTeams = participants.size() / teamSize;

        LetsTeamUpApplication.logMessage("Forming " + numberOfTeams + " skill-based teams");

        for (int i = 0; i < numberOfTeams; i++) {
            formedTeams.add(new Team("T" + (i + 1), teamSize));
        }

        boolean reverse = false;
        int participantIndex = 0;

        while (participantIndex < sorted.size()) {
            List<Team> order = new ArrayList<>(formedTeams);
            if (reverse) {
                Collections.reverse(order);
            }

            for (Team team : order) {
                if (participantIndex < sorted.size() && !team.isFull()) {
                    Participant p = sorted.get(participantIndex);

                    long leaderCount = team.getMembers().stream()
                            .filter(m -> m.getPersonalityType().equals("Leader"))
                            .count();

                    if (p.getPersonalityType().equals("Leader") && leaderCount >= 2) {
                        participantIndex++;
                        continue;
                    }

                    team.addMember(p);
                    participantIndex++;
                }
            }

            reverse = !reverse;
        }

        return new ArrayList<>(formedTeams);
    }
    // Forms teams by grouping participants based on their preferred roles and distributing
    // them evenly across teams, while ensuring no team exceeds leader limits.
    public List<Team> formRoleBasedTeams(List<Participant> participants, int teamSize)
            throws InsufficientParticipantsException {

        validateInput(participants, teamSize);

        formedTeams.clear();

        Map<String, List<Participant>> byRole = participants.stream()
                .collect(Collectors.groupingBy(Participant::getPreferredRole));

        int numberOfTeams = participants.size() / teamSize;

        LetsTeamUpApplication.logMessage("Forming " + numberOfTeams + " role-based teams");

        for (int i = 0; i < numberOfTeams; i++) {
            formedTeams.add(new Team("T" + (i + 1), teamSize));
        }

        for (Map.Entry<String, List<Participant>> entry : byRole.entrySet()) {
            List<Participant> roleParticipants = entry.getValue();
            int teamIndex = 0;

            for (Participant p : roleParticipants) {
                while (teamIndex < formedTeams.size() &&
                        formedTeams.get(teamIndex).isFull()) {
                    teamIndex++;
                }

                if (teamIndex < formedTeams.size()) {
                    Team team = formedTeams.get(teamIndex);

                    long leaderCount = team.getMembers().stream()
                            .filter(m -> m.getPersonalityType().equals("Leader"))
                            .count();

                    if (p.getPersonalityType().equals("Leader") && leaderCount >= 2) {
                        teamIndex++;
                        if (teamIndex >= formedTeams.size()) {
                            teamIndex = 0;
                        }
                        continue;
                    }

                    team.addMember(p);
                    teamIndex++;
                    if (teamIndex >= formedTeams.size()) {
                        teamIndex = 0;
                    }
                }
            }
        }

        return new ArrayList<>(formedTeams);
    }
    // Validates team formation requirements by checking participant count, team size,
    // and ensuring enough leaders exist to distribute across all teams.
    private void validateInput(List<Participant> participants, int teamSize)
            throws InsufficientParticipantsException {

        if (participants == null || participants.isEmpty()) {
            throw new InsufficientParticipantsException("No participants available");
        }

        if (teamSize < 3) {
            throw new InsufficientParticipantsException("Team size must be at least 3");
        }

        if (participants.size() < teamSize) {
            throw new InsufficientParticipantsException(
                    "Need at least " + teamSize + " participants. Available: " + participants.size()
            );
        }

        long leaderCount = participants.stream()
                .filter(p -> p.getPersonalityType().equals("Leader"))
                .count();

        int numberOfTeams = participants.size() / teamSize;
        if (leaderCount < numberOfTeams) {
            throw new InsufficientParticipantsException(
                    "Insufficient leaders. Need at least " + numberOfTeams + " leaders, have " + leaderCount
            );
        }
    }

    public List<Team> getFormedTeams() {
        return new ArrayList<>(formedTeams);
    }
    // Computes aggregated statistics for all formed teams, including averages and
    // distributions of personality types, roles, games, skills, and team sizes.
    public Map<String, Object> calculateStatistics(List<Team> teams) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalTeams", teams.size());

        int totalMembers = teams.stream()
                .mapToInt(Team::getCurrentSize)
                .sum();
        stats.put("totalMembers", totalMembers);

        double avgTeamSize = teams.stream()
                .mapToInt(Team::getCurrentSize)
                .average()
                .orElse(0.0);
        stats.put("avgTeamSize", avgTeamSize);

        double avgSkill = teams.stream()
                .flatMap(t -> t.getMembers().stream())
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(0.0);
        stats.put("avgSkillLevel", avgSkill);

        double avgDiversity = teams.stream()
                .mapToInt(Team::getDiversityScore)
                .average()
                .orElse(0.0);
        stats.put("avgDiversity", avgDiversity);

        Map<String, Integer> personalityDist = new HashMap<>();
        teams.stream()
                .flatMap(t -> t.getMembers().stream())
                .forEach(p -> personalityDist.merge(p.getPersonalityType(), 1, Integer::sum));
        stats.put("personalityDistribution", personalityDist);

        Map<String, Integer> roleDist = new HashMap<>();
        teams.stream()
                .flatMap(t -> t.getMembers().stream())
                .forEach(p -> roleDist.merge(p.getPreferredRole(), 1, Integer::sum));
        stats.put("roleDistribution", roleDist);

        Map<String, Integer> gameDist = new HashMap<>();
        teams.stream()
                .flatMap(t -> t.getMembers().stream())
                .forEach(p -> gameDist.merge(p.getPreferredGame(), 1, Integer::sum));
        stats.put("gameDistribution", gameDist);

        return stats;
    }
    // Safely shuts down the executor service by waiting for ongoing tasks for finishing,
    // forcing termination if needed and handling interruptions gracefully.
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}