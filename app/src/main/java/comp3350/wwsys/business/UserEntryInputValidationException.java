package comp3350.wwsys.business;

/**
 * Custom Exception thrown when user entry input validation fails
 */
public class UserEntryInputValidationException extends Exception {
    public UserEntryInputValidationException(String message) {
        super(message);
    }
}