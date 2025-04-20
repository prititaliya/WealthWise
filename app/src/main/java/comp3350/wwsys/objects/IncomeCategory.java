package comp3350.wwsys.objects;

/**
 * Enum representing categories for income entries
 */
public enum IncomeCategory {
    FULL_TIME_JOB("Full Time Job"),
    PART_TIME_JOB("Part Time Job"),
    ALLOWANCE("Allowance"),
    INVESTMENTS("Investments"),
    OTHER("Other");

    private final String categoryName;

    IncomeCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets the enum value from a display name string.
     *
     * @param categoryName the display name to look up
     * @return the matching enum value, or OTHER if not found
     */
    public static IncomeCategory fromCategoryName(String categoryName) {
        for (IncomeCategory category : values()) {
            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return OTHER;
    }
}