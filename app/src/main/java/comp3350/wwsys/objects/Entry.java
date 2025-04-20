package comp3350.wwsys.objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import comp3350.wwsys.business.StringConfig;

/**
 * Entry Class:
 * Class for a financial entry (income or expense)
 */

public class Entry {
    private final UUID id;                       // Database-generated primary key
    private UUID userId;                         // Reference to the user who owns this entry
    private float amount;
    private String description;
    private String category;
    private EntryType type;                      // INCOME or EXPENSE
    private final LocalDateTime createdDate;    // Timestamp when the entry was created
    private LocalDateTime effectiveDate;        // Timestamp when the entry is effective (past/present/future)


    // ---------- CONSTRUCTORS ----------
    /**
     * Constructor for new income/expense entries with a specified effective date
     */
    public Entry(UUID userId, float amount, String description, String category, String type, LocalDateTime effectiveDate) {
        // id will be set by the database on insert
        this.id = UUID.randomUUID();
        this.createdDate = LocalDateTime.now();
        setDefaults(userId, amount, description, category, type, effectiveDate);
    }

    /**
     * Constructor for existing entries (loaded from database)
     */
    public Entry(UUID id, UUID userId, float amount, String description, String category, String type, LocalDateTime createdDate, LocalDateTime effectiveDate) {
        this.id = id;
        this.createdDate = createdDate;
        setDefaults(userId, amount, description, category, type, effectiveDate);
    }

    /**
     * Helper method to set default values for all constructors
     */
    private void setDefaults(UUID userId, float amount, String description, String category, String type, LocalDateTime effectiveDate) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.effectiveDate = effectiveDate;
        setEntryType(type);
    }

    /**
     * Helper method to set entryType for the entry
     */
    private void setEntryType(String type) {
        if (type == null) {
            this.type = null; // Handle null value appropriately (or set a default type if desired)
        } else if (type.equalsIgnoreCase(StringConfig.INCOME_TYPE)) {
            this.type = EntryType.INCOME;
        } else if (type.equalsIgnoreCase(StringConfig.EXPENSE_TYPE)) {
            this.type = EntryType.EXPENSE;
        } else {
            this.type = null; // Handle invalid type
        }
    }

    /**
     * The type of Entry the object is
     */
    public enum EntryType {
        INCOME,
        EXPENSE
    }

    // ---------- GETTERS/SETTERS ----------
    public UUID getId() {
        return id;
    }
    public UUID getUserId() { return userId; }
    public float getAmount() { return amount; }
    public String getDescription() {
        return description;
    }
    public String getCategory() {
        return category;
    }
    public EntryType getType() {
        return type;
    }

    public String getCreatedDateString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StringConfig.DATE_TIME_FORMAT);
        return createdDate.format(formatter);
    }

    public String getEffectiveDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StringConfig.DATE_TIME_FORMAT);
        return effectiveDate.format(formatter);
    }

    public String getTypeString() {
        if (type == null) {
            return null; // Or return a default value like "unknown" or StringConfig.EXPENSE_TYPE
        } else if (type.equals(EntryType.INCOME)) {
            return StringConfig.INCOME_TYPE;
        } else {
            return StringConfig.EXPENSE_TYPE;
        }
    }

    public LocalDateTime getCreatedDate() { return createdDate; }

    public LocalDateTime getEffectiveDate() { return effectiveDate; }

} // Entry Class