package de.bachlorarbeit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class contains the exception that is returned if a table was not found.
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TableNotFoundException extends RuntimeException {

        /**
         * Creates a new exception with the given parameters.
         * @param message the message to display when the exception is thrown
         */
        public TableNotFoundException(String message) {
            super(message);
        }

        /**
         * Creates a new exception with the given parameters.
         * @param message the message to display when the exception is thrown
         * @param cause the original exception that caused the error
         */
        public TableNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
