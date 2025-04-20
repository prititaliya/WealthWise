package comp3350.wwsys.persistence;


import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import comp3350.wwsys.objects.Entry;

/**
 * EntryPersistence - Interface defining data access methods for managing entries.
 * Provides methods for inserting, updating, deleting, and retrieving income and expense entries.
 */
public interface EntryPersistence {


    /**
     * Inserts a new entry into the database.
     *
     * @param currentEntry the entry to be inserted
     * @return a status message or identifier of the inserted entry
     */
    String insertEntry(Entry currentEntry);

    /**
     * Updates an existing entry in the database.
     *
     * @param currentEntry the entry to be updated
     * @return a status message indicating success or failure
     */
    String updateEntry(Entry currentEntry);

    /**
     * Deletes an entry from the database.
     *
     * @param currentEntry the entry to be deleted
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteEntry(Entry currentEntry);

    /**
     * Retrieves the total amount of a specific type (income/expense) for a given user in the current month.
     *
     * @param userID the unique identifier of the user
     * @param type the type of entry ("income" or "expense")
     * @return the total amount for the specified type and user
     */
    float getMonthTotal(UUID userID, String type);

    /**
     * Retrieves a list of all expense entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @return a list of expense entries for the user
     */
    ArrayList<Entry> userExpenseEntriesObject(UUID userID);

    /**
     * Retrieves a list of all income entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @return a list of income entries for the user
     */
    ArrayList<Entry> userIncomeEntriesObject(UUID userID);

    ArrayList<Entry> getPast5yearTotalIncome(UUID userID);

    ArrayList<Entry> getPast5yearTotalExpense(UUID userID);

    ArrayList<Entry> getPastWeekEntryTotalIncome(UUID userID);

    ArrayList<Entry> getPastWeekEntryTotalExpense(UUID userID);

    ArrayList<Entry> getPastYearEntryTotalIncome(UUID userID);

    ArrayList<Entry> getPastYearEntryTotalExpense(UUID userID);

    /**
     * Retrieves a limited number of income entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @param limit the maximum number of entries to retrieve
     * @return a list of the most recent income entries, limited by the specified count
     */
    ArrayList<Entry> userIncomeEntriesObject(UUID userID, int limit);

    /**
     * Retrieves a limited number of expense entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @param count the maximum number of entries to retrieve
     * @return a list of the most recent expense entries, limited by the specified count
     */
    ArrayList<Entry> userExpenseEntriesObject(UUID userID, int count);

    Map<String, Float> getPieChartDataExpenseWeek(UUID userID);
    Map<String, Float> getPieChartDataIncomeWeek(UUID userID);

    Map<String, Float> getPieChartDataExpenseMonth(UUID userID);
    Map<String, Float> getPieChartDataIncomeMonth(UUID userID);

    Map<String, Float> getPieChartDataIncomeFiveYear(UUID userID);
    Map<String, Float> getPieChartDataExpenseFiveYear(UUID userID);

    float getPieChartDataExpenseWeekTotal(UUID userID);
    float getPieChartDataIncomeWeekTotal(UUID userID);

    float getPieChartDataExpenseMonthTotal(UUID userID);
    float getPieChartDataIncomeMonthTotal(UUID userID);

    float getPieChartDataExpenseFiveYearTotal(UUID userID);
    float getPieChartDataIncomeFiveYearTotal(UUID userID);
}
