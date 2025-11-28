package com.letsteamup.exception;

/**
 * Exception thrown when file processing errors occur
 */
public class FileProcessingException extends Exception {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}