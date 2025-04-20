package comp3350.wwsys.business;

import java.time.LocalDateTime;
import java.util.UUID;

import comp3350.wwsys.objects.Entry;

/**
 * EntryValidator Class:
 * Responsible for validating Entry objects and their components according to business rules.
 * This class ensures data integrity before entries are persisted to the database.
 */
public class EntryValidator {
    // Constants
    private static final float MIN_AMOUNT = 0.01f;
    private static final float MAX_AMOUNT = 1000000.0f;
    private static final int MAX_DESCRIPTION_LENGTH = 250;

    /**
     * Validates all aspects of an entry
     *
     * @param entry The entry to validate
     * @throws EntryValidationException if validation fails
     */
    public static void validateEntry(Entry entry) throws EntryValidationException {
        validateAmount(entry.getAmount());
        validateDescription(entry.getDescription());
        validateCategory(entry.getCategory(), entry.getType());
        validateEffectiveDate(entry.getEffectiveDate());
        validateUserId(entry.getUserId());
    }

    /**
     * Validates that the amount is positive and within acceptable range
     *
     * @param amount The amount to validate
     * @throws EntryValidationException if validation fails
     */
    private static void validateAmount(float amount) throws EntryValidationException {
        if (Float.isNaN(amount)) {
            throw new EntryValidationException(StringConfig.AMOUNT_INVALID_ERROR);
        } else if (amount < MIN_AMOUNT) {
            throw new EntryValidationException(StringConfig.AMOUNT_MIN_ERROR);
        } else if (amount > MAX_AMOUNT) {
            throw new EntryValidationException(StringConfig.AMOUNT_MAX_ERROR);
        }
    }

    /**
     * Validates that the description is not null or too long
     *
     * @param description The description to validate
     * @throws EntryValidationException if validation fails
     */
    private static void validateDescription(String description) throws EntryValidationException {
        if (description == null) {
            throw new EntryValidationException(StringConfig.DESCRIPTION_NULL_ERROR);
        } else if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new EntryValidationException(StringConfig.DESCRIPTION_LENGTH_ERROR);
        }
    }

    /**
     * Validates that the category is appropriate for the entry type
     * @param category The category string to validate
     * @param type The type of entry (INCOME or EXPENSE)
     * @throws EntryValidationException if validation fails
     */
    private static void validateCategory(String category, Entry.EntryType type) throws EntryValidationException {
        if (category == null || category.trim().isEmpty()) {
            throw new EntryValidationException(StringConfig.CATEGORY_EMPTY_ERROR);
        } else if (type == null) {
            throw new EntryValidationException(StringConfig.ENTRY_TYPE_NULL_ERROR);
        }
    }

    /**
     * Validates the user ID of an entry
     *
     * @param userId the user ID to validate
     * @throws EntryValidationException if validation fails
     */
    private static void validateUserId(UUID userId) throws EntryValidationException {
        UserService users = new UserService();
        if (userId == null) {
            throw new EntryValidationException(StringConfig.USER_ID_NULL_ERROR);
        } else if (!users.doesUserExist(userId)) {
            throw new EntryValidationException(StringConfig.USER_NOT_EXIST_ERROR);
        }
    }

    /**
     * Validates that the effective date is not null
     * @param effectiveDate The date to validate
     * @throws EntryValidationException if validation fails
     */
    private static void validateEffectiveDate(LocalDateTime effectiveDate) throws EntryValidationException {
        if (effectiveDate == null) {
            throw new EntryValidationException(StringConfig.DATE_REQUIRED_ERROR);
        }
    }
} // Entry Validator