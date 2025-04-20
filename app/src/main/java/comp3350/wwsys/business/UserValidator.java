package comp3350.wwsys.business;

import comp3350.wwsys.objects.User;

/**
 * UserValidator - Handles input validation for user-related fields.
 * Provides various methods to validate user details such as email, password,
 * first name, and last name.
 */
public class UserValidator {

    /**
     * Validates only an email and a password
     *
     * @param email    the user's email
     * @param password the user's password
     * @throws UserValidationException Email is empty or wrong format
     */
    public static void validateLogin(String email, String password) throws UserValidationException{
        checkEmail(email);
        checkPassword(password);
    }

    /**
     * Validates user details during account insertion/registration.
     *
     * @param email           the user's email address
     * @param password        the user's password
     * @param confirmPassword the confirmation of the user's password
     * @param firstname       the user's first name
     * @param lastname        the user's last name
     * @throws UserValidationException if any validation check fails.
     */
    public static void validateInsert(String email, String password, String confirmPassword, String firstname, String lastname) throws UserValidationException{
        checkFirstName(firstname);
        checkLastName(lastname);
        checkEmail(email);
        checkPassword(password);
        checkPassword(confirmPassword);
        checkPasswordMatch(password,confirmPassword);
    }

    /**
     * Validates and updates user information, ensuring all fields meet requirements.
     * Includes confirmation password verification.
     *
     * @param user            the user object with updated data
     * @param confirmPassword the confirmation of the user's password
     * @throws UserValidationException if any validation check fails.
     */
    public static void updateUser(User user, String confirmPassword) throws UserValidationException {
        checkFirstName(user.getFirstName());
        checkLastName(user.getLastName());
        checkEmail(user.getEmail());
        checkPassword(user.getPassword());
        checkPassword(confirmPassword);
    }

    /**
     * Validates and updates user information without requiring a password confirmation.
     *
     * @param user the user object with updated data
     * @throws UserValidationException if any validation check fails.
     */
    public static void updateUser(User user) throws UserValidationException {
        checkFirstName(user.getFirstName());
        checkLastName(user.getLastName());
        checkEmail(user.getEmail());
        checkPassword(user.getPassword());
    }

    /**
     * Checks the validity of the user's first name.
     *
     * @param firstName the user's first name to validate
     * @throws UserValidationException if the first name is invalid.
     */
    private static void checkFirstName(String firstName) throws UserValidationException {
        String result = validateName(firstName);
        if (result != null) {
            throw new UserValidationException("First " + result);
        }
    }

    /**
     * Checks the validity of the user's last name.
     *
     * @param lastname the user's last name to validate
     * @throws UserValidationException if the last name is invalid.
     */
    private static void checkLastName(String lastname) throws UserValidationException {
        String result = validateName(lastname);
        if (result != null) {
            throw new UserValidationException("Last " + result);
        }
    }

    /**
     * Validates a name string to ensure it meets criteria:
     * - It cannot be null or empty.
     * - It must only contain alphabetic characters (A-Z, a-z).
     *
     * @param name the name to validate
     * @return null if the name is valid, or an error message describing the issue.
     */
    private static String validateName(String name) {
        String result = null;
        if (name == null || name.trim().isEmpty()) {
            result = StringConfig.NAME_EMPTY_ERROR;
        }
        else if (!name.matches("^[A-Za-z ]+$")) {
            result = StringConfig.NAME_FORMAT_ERROR;
        }
        return result;
    }

    /**
     * Checks if the email provided is valid.
     * It ensures that the email is not null, not empty, and matches a standard email pattern.
     *
     * @param email the email to validate
     * @throws UserValidationException if the email is invalid.
     */
    private static void checkEmail(String email) throws UserValidationException {
        String errorString = null;
        if (email == null || email.trim().isEmpty()) {
            errorString = StringConfig.EMAIL_EMPTY_ERROR;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$")) {
            errorString = StringConfig.USER_INVALID_EMAIL + "fuck";
        }

        if (errorString != null) {
            throw new UserValidationException(errorString);
        }
    }

    /**
     * Checks if the password meets specific criteria:
     * - It cannot be null.
     * - It must be at least 6 characters long.
     * - It must contain at least one uppercase letter.
     * - It must contain at least one special character.
     *
     * @param password the password to validate
     * @throws UserValidationException if the password does not meet the required criteria.
     */
    private static void checkPassword(String password) throws UserValidationException {
        if (password == null) {
            throw new UserValidationException(StringConfig.PASSWORD_NULL_ERROR);
        } else if (password.length() < 6) {
            throw new UserValidationException(StringConfig.PASSWORD_LENGTH_ERROR);
        } else if (!password.matches(".*[A-Z].*")) {
            throw new UserValidationException(StringConfig.PASSWORD_UPPERCASE_ERROR);
        } else if (!password.matches(".*[a-z].*")) {
            throw new UserValidationException(StringConfig.PASSWORD_LOWERCASE_ERROR);
        } else if (!password.matches(".*[0-9].*")) {
            throw new UserValidationException(StringConfig.PASSWORD_NUMBER_ERROR);
        } else if (!password.matches(".*[!@#$%^&*()_+<>?/].*")) {
            throw new UserValidationException(StringConfig.PASSWORD_SPECIAL_ERROR);
        }
    }

    /**
     * Ensures that the password and confirmation password match.
     *
     * @param password        the original password
     * @param confirmPassword the password confirmation
     * @throws UserValidationException if the passwords do not match.
     */
    public static void checkPasswordMatch(String password, String confirmPassword) throws UserValidationException {
        if (!password.equals(confirmPassword)) {
            throw new UserValidationException(StringConfig.USER_PASSWORD_MISMATCH);
        }
    }

    /**
     * Ensures that the password and confirmation password match.
     *
     * @param user - user to get password
     * @param password - the original password
     * @throws UserValidationException if the passwords do not match.
     */
    public static void checkCurrentPassword(User user, String password) throws UserValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new UserValidationException(StringConfig.PASSWORD_NULL_ERROR);
        }
        if (!password.equals(user.getPassword())) {
            throw new UserValidationException(StringConfig.PASSWORD_INCORRECT_ERROR);
        }
    }


}