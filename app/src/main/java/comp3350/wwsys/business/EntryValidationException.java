package comp3350.wwsys.business;

/**
 * Custom Exception thrown when entry validation fails
 */
public class EntryValidationException extends Exception {
    public EntryValidationException(String message) {
        super(message);
    }
}