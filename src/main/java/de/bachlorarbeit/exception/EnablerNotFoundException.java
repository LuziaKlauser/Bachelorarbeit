package de.bachlorarbeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnablerNotFoundException extends RuntimeException{
    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     */
    public EnablerNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     * @param cause the original exception that caused the error
     */
    public EnablerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
