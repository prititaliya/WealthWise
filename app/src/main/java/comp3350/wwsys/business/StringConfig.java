package comp3350.wwsys.business;

/**
 * String Configuration for business layer strings.
 * Centralizes string constants to improve maintainability and consistency.
 */
public class StringConfig {
    // Entry types
    public static final String INCOME_TYPE = "Income"; // Persistence
    public static final String EXPENSE_TYPE = "Expense"; // Persistence

    // Duration types
    public static final String DURATION_WEEK = "week";
    public static final String DURATION_MONTH = "month";
    public static final String DURATION_YEAR = "year";
    // Formatting
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Entry Service: General Error Messages
    public static final String DATA_RETRIEVAL_ERROR = "Unable to retrieve data."; // replacing: "entries list is null." and "Entry cannot be null."
    public static final String ENTRY_DELETE_ERROR = "Entry could not be deleted."; // replacing: "Entry not found or could not be deleted."
    public static final String NO_ENTRIES_TYPE_ERROR = "No %s entries found.";
    // replacing: "No income entries found for user.", "No expense entries found for user.", and
    // "No income entries found for the user.", "No expense transactions found for the user."

    // Entry Service: Chart and Visualization Errors
    public static final String CHART_DATA_NULL_ERROR = "No data available for visualization."; // replacing: "Null Pie entries"
    public static final String NO_TYPE_ENTRIES_DISPLAY_ERROR = "No %s entries to display."; // for getWeeklyData

    // Entry Validator: Error Messages
    public static final String AMOUNT_INVALID_ERROR = "Amount must be a valid number."; // replacing: "Amount must be a valid number"
    public static final String AMOUNT_MIN_ERROR = "Amount must be at least 0.01."; // replacing: "Amount must be at least " + MIN_AMOUNT
    public static final String AMOUNT_MAX_ERROR = "Amount cannot exceed 1,000,000."; // replacing: "Amount cannot exceed " + MAX_AMOUNT
    public static final String DESCRIPTION_NULL_ERROR = "Description cannot be found."; // replacing: "Description cannot be null"
    public static final String DESCRIPTION_LENGTH_ERROR = "Description is too long."; // replacing: "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters"
    public static final String CATEGORY_EMPTY_ERROR = "Please select a category."; // replacing: "Category cannot be empty"
    public static final String ENTRY_TYPE_NULL_ERROR = "Please select an entry type."; // replacing: "Entry type cannot be null"
    public static final String USER_ID_NULL_ERROR = "User ID is missing."; // replacing: "User ID null error"
    public static final String USER_NOT_EXIST_ERROR = "User does not exist."; // replacing: "User does not Exist"
    public static final String DATE_REQUIRED_ERROR = "Please select a date."; // replacing: "Date Selection Required"

    // User Service: Errors
    public static final String USER_INCORRECT_CREDENTIALS = "Incorrect username or password."; //
    public static final String USER_NOT_FOUND = "User account not found."; // replacing: "User Account Not Found" and "User cannot be found"
    public static final String USER_NO_CURRENT_USER = "No user was found."; // replacing: "No User found"
    public static final String USER_NULL_ERROR = "Unable to retrieve user data."; // replacing: null check messages
    public static final String USER_UPDATE_ERROR = "Could not update user information.";
    public static final String USER_DELETE_ERROR = "Could not delete user account.";
    public static final String USER_LOGIN_ERROR = "Could not complete login.";

    // User Validator: Error Messages
    public static final String USER_PASSWORD_MISMATCH = "Passwords do not match.";
    public static final String USER_INVALID_EMAIL = "Please enter a valid email address."; // replacing: "Invalid email format"
    public static final String USER_EMAIL_EXISTS = "An account with this email already exists."; // replacing: duplicate email errors from persistence
    public static final String NAME_EMPTY_ERROR = "Name cannot be empty."; // replacing: "cannot be empty" for names
    public static final String NAME_FORMAT_ERROR = "Name can only contain letters."; // replacing: "cannot contain numbers or special characters"
    public static final String EMAIL_EMPTY_ERROR = "Email address cannot be empty."; // replacing: "Email Cannot be Empty"
    public static final String PASSWORD_NULL_ERROR = "Password cannot be empty."; // replacing: "Password Cannot be Null" and "Password cannot be empty"
    public static final String PASSWORD_LENGTH_ERROR = "Password must be at least 6 characters."; // replacing: "Password needs 6 Characters or more"
    public static final String PASSWORD_UPPERCASE_ERROR = "Password must contain at least one uppercase letter."; // replacing: "Password does not contain Uppercase Letter"
    public static final String PASSWORD_SPECIAL_ERROR = "Password must contain at least one special character."; // replacing: "Password does not contain Special Character"
    public static final String PASSWORD_INCORRECT_ERROR = "Incorrect password."; // replacing: "Password Incorrect"
    // New password validation constants
    public static final String PASSWORD_LOWERCASE_ERROR = "Password must contain at least one lowercase letter."; // for password lowercase validation
    public static final String PASSWORD_NUMBER_ERROR = "Password must contain at least one number."; // for password number validation

} //StringConfig