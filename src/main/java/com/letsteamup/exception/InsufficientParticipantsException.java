package com.letsteamup.exception;

/**
 * Exception thrown when there are not enough participants to form teams
 */
public class InsufficientParticipantsException extends Exception {
    public InsufficientParticipantsException(String message) {
        super(message);
    }

    public InsufficientParticipantsException(String message, Throwable cause) {
        super(message, cause);
    }
}