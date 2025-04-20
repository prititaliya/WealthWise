package comp3350.wwsys.business;

/**
 * ValidateUserEntryInput: A utility class for validating user input entries,
 * including amounts and password changes.
 */
public class ValidateUserEntryInput {

    /**
     * Validates and parses a given amount (String) into a float.
     * If the amount is not valid, it throws a UserEntryInputValidationException.
     *
     * @param amount The string representing the amount to be validated.
     * @return The parsed float value of the given amount.
     * @throws UserEntryInputValidationException If the amount is invalid (non-numeric).
     */
    public static float validateAmount(String amount) throws UserEntryInputValidationException {
        try {
            return Float.parseFloat(amount);
        } catch (NumberFormatException e) {
            throw new UserEntryInputValidationException("Invalid Amount");
        }
    }

    /**
     * Validates the old password during a password change.
     * If the old password is empty, it throws a UserEntryInputValidationException.
     *
     * @param oldPassword The old password string to be validated.
     * @throws UserEntryInputValidationException If the old password is empty.
     */
    public static void validatePasswordChange(String oldPassword) throws UserEntryInputValidationException {
        if (oldPassword.isEmpty())
            throw new UserEntryInputValidationException("Current Password Is Incorrect");
    }




    }
