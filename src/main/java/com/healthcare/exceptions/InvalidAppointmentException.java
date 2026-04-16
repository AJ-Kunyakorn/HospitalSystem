package com.healthcare.exceptions;

/**
 * Custom checked exception for invalid appointment operations.
 */
public class InvalidAppointmentException extends Exception {
    /**
     * Constructor for InvalidAppointmentException.
     *
     * @param message the error message
     */
    public InvalidAppointmentException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidAppointmentException with cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public InvalidAppointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
