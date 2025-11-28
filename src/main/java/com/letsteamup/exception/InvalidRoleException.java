package com.letsteamup.exception;

/**
 * Exception thrown when an invalid role is specified
 */
public class InvalidRoleException extends Exception {
    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}