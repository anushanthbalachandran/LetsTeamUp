package com.letsteamup.exception;

/**
 * Exception thrown when an invalid personality score is provided
 */
public class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }

    public InvalidScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}