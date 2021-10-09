package de.bachlorarbeit.exception;

/**
 * This class contains the exception that is returned if a error concerning file storage occurs.
 */
/**
 * Exception thrown when an error occurs while storing a file
 */
public class StorageException extends RuntimeException{

    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given parameters.
     * @param message the message to display when the exception is thrown
     * @param cause the original exception that caused the error
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
