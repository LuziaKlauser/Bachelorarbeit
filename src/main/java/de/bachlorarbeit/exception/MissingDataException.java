package de.bachlorarbeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there the enabler entry with a enabler_id is not found
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingDataException extends RuntimeException{
    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     */
    public MissingDataException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     * @param cause the original exception that caused the error
     */
    public MissingDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
