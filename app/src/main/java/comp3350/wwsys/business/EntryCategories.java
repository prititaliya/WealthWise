package comp3350.wwsys.business;

import comp3350.wwsys.objects.ExpenseCategory;
import comp3350.wwsys.objects.IncomeCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages financial entry categories for different entry types
 * Follows OCP by allowing new entry types to be added without modifying existing code
 */
public class EntryCategories {
    private static final Map<String, Supplier<ArrayList<String>>> CATEGORY_SOURCES = new HashMap<>();

    static {
        // Register income category source
        CATEGORY_SOURCES.put("income", () -> {
            ArrayList<String> categories = new ArrayList<>();
            for (IncomeCategory category : IncomeCategory.values()) {
                categories.add(category.getCategoryName());
            }
            return categories;
        });

        // Register expense category source
        CATEGORY_SOURCES.put("expense", () -> {
            ArrayList<String> categories = new ArrayList<>();
            for (ExpenseCategory category : ExpenseCategory.values()) {
                categories.add(category.getCategoryName());
            }
            return categories;
        });

        // unlikely but if future categories exist register them here
    }

    /**
     * Get all category names for a financial entry type
     * @param entryType The entry type (income, expense, etc.)
     * @return List of category names or empty list if type not found
     */
    public static ArrayList<String> getCategoryNames(String entryType) {
        Supplier<ArrayList<String>> categorySource = CATEGORY_SOURCES.get(entryType.toLowerCase());
        return (categorySource != null) ? categorySource.get() : new ArrayList<>();
    }


} // EntryCategories