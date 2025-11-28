package com.letsteamup.util;

import com.letsteamup.model.Participant;
import com.letsteamup.model.Team;
import com.letsteamup.exception.FileProcessingException;
import com.letsteamup.validator.ParticipantValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    private static final String COMMA_DELIMITER = ",";

    public static List<Participant> readParticipantsFromCSV(String filename)
            throws FileProcessingException {
        List<Participant> participants = new ArrayList<>();
        BufferedReader br = null;

        try {
            File file = new File(filename);
            if (!file.exists()) {
                throw new FileProcessingException("File not found: " + filename);
            }

            br = new BufferedReader(new FileReader(file));

            String header = br.readLine();
            if (header == null) {
                throw new FileProcessingException("CSV file is empty");
            }

            String line;
            int lineNumber = 1;
            int successCount = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Participant participant = parseParticipantLine(line);
                    participants.add(participant);
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Line " + lineNumber + " - " + e.getMessage());
                }
            }

            if (participants.isEmpty()) {
                throw new FileProcessingException(
                        "No valid participants found. Errors: " + errorCount
                );
            }

            System.out.println("Successfully loaded " + successCount + " participants");
            if (errorCount > 0) {
                System.out.println("Skipped " + errorCount + " invalid entries");
            }

        } catch (IOException e) {
            throw new FileProcessingException("Error reading CSV file: " + filename, e);
        } finally {
            closeQuietly(br);
        }

        return participants;
    }

    private static Participant parseParticipantLine(String line) throws Exception {
        String[] values = line.split(COMMA_DELIMITER, -1);

        if (values.length < 8) {
            throw new IllegalArgumentException(
                    "Insufficient columns (expected 8, got " + values.length + ")"
            );
        }

        try {
            String id = values[0].trim();
            String name = values[1].trim();
            String email = values[2].trim();
            String preferredGame = values[3].trim();
            int skillLevel = Integer.parseInt(values[4].trim());
            String preferredRole = values[5].trim();
            int personalityScore = Integer.parseInt(values[6].trim());

            if (id.isEmpty()) throw new IllegalArgumentException("ID is empty");
            ParticipantValidator.validateName(name);
            ParticipantValidator.validateEmail(email);
            ParticipantValidator.validateGame(preferredGame);
            ParticipantValidator.validateSkillLevel(skillLevel);
            ParticipantValidator.validateRole(preferredRole);
            ParticipantValidator.validatePersonalityScore(personalityScore);

            return new Participant(id, name, 20, email, personalityScore,
                    preferredGame, preferredRole, skillLevel);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage());
        }
    }

    public static void writeParticipantsToCSV(List<Participant> participants, String filename)
            throws FileProcessingException {

        BufferedWriter bw = null;

        try {
            File outputFile = new File(filename);

            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new FileProcessingException("Could not create directories for: " + filename);
                }
            }

            bw = new BufferedWriter(new FileWriter(outputFile));

            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();

            for (Participant p : participants) {
                StringBuilder sb = new StringBuilder();
                sb.append(escape(p.getId())).append(COMMA_DELIMITER);
                sb.append(escape(p.getName())).append(COMMA_DELIMITER);
                sb.append(escape(p.getEmail())).append(COMMA_DELIMITER);
                sb.append(escape(p.getPreferredGame())).append(COMMA_DELIMITER);
                sb.append(p.getSkillLevel()).append(COMMA_DELIMITER);
                sb.append(escape(p.getPreferredRole())).append(COMMA_DELIMITER);
                sb.append(p.getPersonalityScore()).append(COMMA_DELIMITER);
                sb.append(escape(p.getPersonalityType()));

                bw.write(sb.toString());
                bw.newLine();
            }

            bw.flush();
            System.out.println("Participants exported to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            throw new FileProcessingException("Error writing to file: " + filename, e);
        } finally {
            closeQuietly(bw);
        }
    }

    public static void writeTeamsToCSV(List<Team> teams, String filename)
            throws FileProcessingException {

        BufferedWriter bw = null;

        try {
            File outputFile = new File(filename);

            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new FileProcessingException("Could not create directories for: " + filename);
                }
            }

            bw = new BufferedWriter(new FileWriter(outputFile));

            bw.write("TeamID,TeamName,TeamSize,MemberID,MemberName,");
            bw.write("Age,Email,PersonalityType,PersonalityScore,PreferredGame,Role,SkillLevel");
            bw.newLine();

            for (Team team : teams) {
                for (Participant member : team.getMembers()) {
                    StringBuilder sb = new StringBuilder();

                    sb.append(escape(team.getTeamId())).append(COMMA_DELIMITER);
                    sb.append(escape(team.getTeamName())).append(COMMA_DELIMITER);
                    sb.append(team.getCurrentSize()).append(COMMA_DELIMITER);

                    sb.append(escape(member.getId())).append(COMMA_DELIMITER);
                    sb.append(escape(member.getName())).append(COMMA_DELIMITER);
                    sb.append(member.getAge()).append(COMMA_DELIMITER);
                    sb.append(escape(member.getEmail())).append(COMMA_DELIMITER);
                    sb.append(escape(member.getPersonalityType())).append(COMMA_DELIMITER);
                    sb.append(member.getPersonalityScore()).append(COMMA_DELIMITER);
                    sb.append(escape(member.getPreferredGame())).append(COMMA_DELIMITER);
                    sb.append(escape(member.getPreferredRole())).append(COMMA_DELIMITER);
                    sb.append(member.getSkillLevel());

                    bw.write(sb.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            System.out.println("Teams exported to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            throw new FileProcessingException("Error writing to file: " + filename, e);
        } finally {
            closeQuietly(bw);
        }
    }

    private static String escape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.err.println("Error closing stream: " + e.getMessage());
            }
        }
    }

    public static boolean validateFile(String filename) {
        File file = new File(filename);
        return file.exists() && file.isFile() && file.canRead();
    }
}