package comp3350.wwsys.objects;

/**
 * Enum representing categories for expense entries
 */
public enum ExpenseCategory {
    SUBSCRIPTIONS("Subscriptions"),
    LIVING_EXPENSES("Living Expenses"),
    TRANSPORTATION("Transportation"),
    HEALTH("Health"),
    FOOD("Food"),
    EDUCATION("Education"),
    SHOPPING("Shopping"),
    OTHER("Other");

    private final String categoryName;

    ExpenseCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets the enum value from a category name string.
     *
     * @param categoryName the category name to look up
     * @return the matching enum value, or OTHER if not found
     */
    public static ExpenseCategory fromCategoryName(String categoryName) {
        for (ExpenseCategory category : values()) {
            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return OTHER;
    }
}