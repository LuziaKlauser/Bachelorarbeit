package de.bachlorarbeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is no Employee Entry with the employeeId
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException{
    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     * @param cause the original exception that caused the error
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

