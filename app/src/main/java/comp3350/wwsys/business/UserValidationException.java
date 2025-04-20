package comp3350.wwsys.business;

/**
 * Custom Exception for handling validation errors.
 * This exception is thrown when user input fails any validation checks.
 */
public class UserValidationException extends Exception {
    public UserValidationException(String message) {
        super(message);
    }
}
