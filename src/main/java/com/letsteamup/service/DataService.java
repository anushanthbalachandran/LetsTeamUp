package com.letsteamup.service;

import com.letsteamup.model.Participant;
import com.letsteamup.model.Team;
import com.letsteamup.util.CSVHandler;
import com.letsteamup.exception.FileProcessingException;
import com.letsteamup.LetsTeamUpApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataService {

    private List<Participant> participants;
    private static final String ALL_PARTICIPANTS_FILE = "src/main/resources/allParticipants.csv";
    private static final String SAMPLE_FILE = "src/main/resources/participants_sample.csv";

    public DataService() {
        this.participants = new ArrayList<>();
    }

    public void addParticipant(Participant participant) {
        if (!isDuplicateEmail(participant.getEmail())) {
            participants.add(participant);
            LetsTeamUpApplication.logMessage("Participant added: " + participant.getId() + " - " + participant.getName());
        } else {
            LetsTeamUpApplication.logMessage("Duplicate email rejected: " + participant.getEmail());
        }
    }

    private boolean isDuplicateEmail(String email) {
        return participants.stream()
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(email));
    }

    public List<Participant> getAllParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public void clearParticipants() {
        participants.clear();
        LetsTeamUpApplication.logMessage("All participants cleared");
    }

    public List<Participant> loadFromCSV(String filename) throws FileProcessingException {
        try {
            List<Participant> loaded = CSVHandler.readParticipantsFromCSV(filename);

            for (Participant p : loaded) {
                addParticipant(p);
            }

            LetsTeamUpApplication.logMessage("Loaded " + loaded.size() + " participants from " + filename);
            return loaded;

        } catch (FileProcessingException e) {
            LetsTeamUpApplication.logMessage("Failed to load from " + filename + ": " + e.getMessage());
            throw e;
        }
    }

    public void loadParticipantsAutomatically() throws FileProcessingException {
        try {
            File allParticipantsFile = new File(ALL_PARTICIPANTS_FILE);

            if (allParticipantsFile.exists() && allParticipantsFile.length() > 100) {
                loadFromCSV(ALL_PARTICIPANTS_FILE);
                LetsTeamUpApplication.logMessage("Loaded participants from allParticipants.csv");
            } else {
                loadFromCSV(SAMPLE_FILE);
                LetsTeamUpApplication.logMessage("allParticipants.csv empty or missing, loaded from participants_sample.csv");
            }
        } catch (FileProcessingException e) {
            LetsTeamUpApplication.logMessage("Automatic load failed: " + e.getMessage());
            throw e;
        }
    }

    public void saveToAllParticipants() throws FileProcessingException {
        try {
            File allParticipantsFile = new File(ALL_PARTICIPANTS_FILE);
            List<Participant> existingParticipants = new ArrayList<>();

            if (allParticipantsFile.exists() && allParticipantsFile.length() > 100) {
                existingParticipants = CSVHandler.readParticipantsFromCSV(ALL_PARTICIPANTS_FILE);
                LetsTeamUpApplication.logMessage("Loaded " + existingParticipants.size() + " existing participants");
            }

            for (Participant newP : participants) {
                boolean exists = existingParticipants.stream()
                        .anyMatch(p -> p.getEmail().equalsIgnoreCase(newP.getEmail()));
                if (!exists) {
                    existingParticipants.add(newP);
                }
            }

            CSVHandler.writeParticipantsToCSV(existingParticipants, ALL_PARTICIPANTS_FILE);
            LetsTeamUpApplication.logMessage("Saved " + existingParticipants.size() + " total participants to allParticipants.csv");
        } catch (FileProcessingException e) {
            LetsTeamUpApplication.logMessage("Failed to save to allParticipants.csv: " + e.getMessage());
            throw e;
        }
    }

    public void exportTeamsToCSV(List<Team> teams, String filename) throws FileProcessingException {
        String fullPath = "src/main/resources/" + filename;
        CSVHandler.writeTeamsToCSV(teams, fullPath);
        LetsTeamUpApplication.logMessage("Exported " + teams.size() + " teams to " + fullPath);
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public Participant findById(String id) {
        return participants.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}