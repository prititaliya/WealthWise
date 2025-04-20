package comp3350.wwsys.business;

import static comp3350.wwsys.business.DateGetter.getListOfMonths;
import static comp3350.wwsys.business.DateGetter.getListOfWeek;
import static comp3350.wwsys.business.DateGetter.getListOfYears;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import comp3350.wwsys.application.Services;
import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.EntryPersistence;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Map;

/*
 *EntryService class handles the logic for entry related services.
 */
public class EntryService {
    private final EntryPersistence entryPersistence;
    /**
     * Constructor for EntryService
     */
    public EntryService() {
        this.entryPersistence = Services.getEntryPersistence(); // ties to our database
    }

    /**
     * Constructor for stub database EntryService
     */
    public EntryService(EntryPersistence entryPersistence) {
        this.entryPersistence = entryPersistence;
    }

    /**
     * Adds a new entry after validation.
     * (higher level service logic)
     *
     * @throws EntryValidationException if the entry fails validation.
     */
    public void addEntry(User user, float amount, String description, String category, String type, LocalDateTime effectiveDate) throws EntryValidationException {

        Entry entry = new Entry(user.getUserID(), amount, description, category, type, effectiveDate);
        EntryValidator.validateEntry(entry);
        String result = entryPersistence.insertEntry(entry);
        if (result != null) {
            throw new EntryValidationException(result);
        }
    }

    /**
     * Retrieves the total monthly income for a specific user.
     *
     * @param user The user whose monthly income is to be retrieved.
     * @return The total monthly income for the user. Returns 0.00 if the user is null.
     */
    public float getMonthlyIncome(User user) {
        float result = 0.00f;
        if (user != null) {
            result = entryPersistence.getMonthTotal(user.getUserID(), StringConfig.INCOME_TYPE);
        }
        return result;
    }

    /**
     * Retrieves the total monthly Expense for a specific user.
     *
     * @param user The user whose monthly income is to be retrieved.
     * @return The total monthly Expense for the user. Returns 0.00 if the user is null.
     */
    public float getMonthlyExpense(User user) {
        float result = 0.00f;
        if (user != null) {
            result = entryPersistence.getMonthTotal(user.getUserID(), StringConfig.EXPENSE_TYPE);
        }
        return result;
    }

    /**
     * Retrieves the net monthly balance for a specific user.
     * This is calculated as monthly income minus monthly expenses.
     *
     * @param user The user whose net monthly balance is to be retrieved.
     * @return The net monthly balance for the user. Returns 0.00 if the user is null.
     */
    public float getMonthlyNet(User user) {
        float result = 0.00f;
        if (user != null) {
            result = getMonthlyIncome(user) - getMonthlyExpense(user);
        }
        return result;
    }

    /**
     * Retrieves weekly data for bar chart visualization.
     *
     * @param user The user whose data is being retrieved
     * @param type The type of entries to retrieve (income or expense)
     * @return A list of BarEntry objects representing weekly data
     * @throws EntryValidationException If no entries are found for the selected period
     */
    public ArrayList<BarEntry> getWeeklyData(User user, String type) throws EntryValidationException {
        ArrayList<Entry> entries = new ArrayList<>();
        if (type.equals(StringConfig.INCOME_TYPE)) {
            entries = entryPersistence.getPastWeekEntryTotalIncome(user.getUserID());
        } else if (type.equals(StringConfig.EXPENSE_TYPE)) {
            entries = entryPersistence.getPastWeekEntryTotalExpense(user.getUserID());
        }

        DayOfWeek[] week = getListOfWeek();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        int dayOrder = 0;
        for (DayOfWeek day : week) {
            float amount = 0;

            for(Entry entry: entries) {
                if (entry.getEffectiveDate().getDayOfWeek().equals(day)){
                    amount += entry.getAmount();
                }
            }

            barEntries.add(new BarEntry(dayOrder, amount));
            dayOrder++;
        }

        if (barEntries.isEmpty()) {
            throw new EntryValidationException(String.format(StringConfig.NO_TYPE_ENTRIES_DISPLAY_ERROR, type));
        }

        return barEntries;
    }

    /**
     * Retrieves monthly data for bar chart visualization.
     *
     * @param user The user whose data is being retrieved
     * @param type The type of entries to retrieve (income or expense)
     * @return A list of BarEntry objects representing monthly data
     */
    public ArrayList<BarEntry> getMonthlyData(User user, String type) {
        ArrayList<Entry> entries = new ArrayList<>();
        if (type.equals(StringConfig.INCOME_TYPE)) {
            entries = entryPersistence.getPastYearEntryTotalIncome(user.getUserID());
        } else if (type.equals(StringConfig.EXPENSE_TYPE)) {
            entries = entryPersistence.getPastYearEntryTotalExpense(user.getUserID());
        }

        Month[] week = getListOfMonths();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        int dayOrder = 0;
        for (Month month : week) {
            float amount = 0;
            for(Entry entry: entries) {
                if (entry.getEffectiveDate().getMonth().equals(month)){
                    amount += entry.getAmount();
                }
            }
            barEntries.add(new BarEntry(dayOrder, amount));
            dayOrder++;
        }

        return barEntries;
    }

    /**
    * Retrieves yearly data for bar chart visualization.
    *
    * @param user The user whose data is being retrieved
    * @param type The type of entries to retrieve (income or expense)
    * @return A list of BarEntry objects representing yearly data
    */
    public ArrayList<BarEntry> getYearData(User user, String type) {
        ArrayList<Entry> entries = new ArrayList<>();
        if (type.equals(StringConfig.INCOME_TYPE)) {
            entries = entryPersistence.getPast5yearTotalIncome(user.getUserID());
        } else if (type.equals(StringConfig.EXPENSE_TYPE)) {
            entries = entryPersistence.getPast5yearTotalExpense(user.getUserID());
        }

        Year[] years = getListOfYears();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        int dayOrder = 0;
        for (Year year: years) {
            float amount = 0;
            for(Entry entry: entries) {
                if (entry.getEffectiveDate().getYear() == year.getValue()){
                    amount += entry.getAmount();
                }
            }
            barEntries.add(new BarEntry(dayOrder, amount));
            dayOrder++;
        }

        return barEntries;
    }

    /**
     * Retrieves ALL income entries for a specific user.
     * Used for "See More" option in tracking dashboard UI
     *
     * @param user The User object whose income entries we want to fetch.
     * @return A list of income entries for the user.
     */
    public ArrayList<Entry> getIncomeEntries(User user) throws EntryValidationException {
        ArrayList<Entry> incomeEntries = entryPersistence.userIncomeEntriesObject(user.getUserID());

        if (incomeEntries.isEmpty()) {
            throw new EntryValidationException(String.format(StringConfig.NO_ENTRIES_TYPE_ERROR, StringConfig.INCOME_TYPE));
        }
        return incomeEntries;
    }

    /**
     * Retrieves ALL expense entries for a specific user.
     * Used for "See More" option in tracking dashboard UI
     *
     * @param user The User object whose expense entries we want to fetch.
     * @return A list of expense entries for the user.
     */
    public ArrayList<Entry> getExpenseEntries(User user) throws EntryValidationException {
        ArrayList<Entry> expenseEntries = entryPersistence.userExpenseEntriesObject(user.getUserID());
        if (expenseEntries.isEmpty()) {
            throw new EntryValidationException(String.format(StringConfig.NO_ENTRIES_TYPE_ERROR, StringConfig.EXPENSE_TYPE));
        }
        return expenseEntries;
    }

    /**
     * Retrieves the most recent income entries for a specific user.
     * Will be used in tracking dashboard UI
     *
     * @param user  The User object whose income entries we want to fetch.
     * @param count The number of recent entries to retrieve.
     * @return A list of the most recent income entries for the specified user.
     * @throws EntryValidationException if no income entries are found for the user.
     */
    public ArrayList<Entry> getMostRecentIncomeEntries(User user, int count) throws EntryValidationException {
        ArrayList<Entry> incomeEntries = entryPersistence.userIncomeEntriesObject(user.getUserID(), count);

        if (incomeEntries.isEmpty()) {
            throw new EntryValidationException(String.format(StringConfig.NO_ENTRIES_TYPE_ERROR, StringConfig.INCOME_TYPE));
        }
        return incomeEntries;
    }

    /**
     * Retrieves the most recent expense entries for a specific user.
     * Will be used in tracking dashboard UI
     *
     * @param user  The User object whose expense entries we want to fetch.
     * @param count The number of recent entries to retrieve.
     * @return A list of the most recent expense entries for the specified user.
     * @throws EntryValidationException if no expense entries are found for the user.
     */
    public ArrayList<Entry> getMostRecentExpenseEntries(User user, int count) throws EntryValidationException {
        ArrayList<Entry> expenseEntries = entryPersistence.userExpenseEntriesObject(user.getUserID(), count);
        if (expenseEntries.isEmpty()) {
            throw new EntryValidationException(String.format(StringConfig.NO_ENTRIES_TYPE_ERROR, StringConfig.EXPENSE_TYPE));
        }
        return expenseEntries;
    }

    /**
     * Removes an entry from the system after validation.
     *
     * @param entry The entry to be removed.
     * @throws EntryValidationException If the entry is null or cannot be deleted.
     */
    public void removeEntry(Entry entry) throws EntryValidationException {
        if (entry != null) {
            boolean deleted = entryPersistence.deleteEntry(entry);
            if (!deleted) {
                throw new EntryValidationException(StringConfig.ENTRY_DELETE_ERROR);
            }
        } else {
            throw new EntryValidationException(StringConfig.DATA_RETRIEVAL_ERROR);
        }
    }

    /**
     * Updates an existing entry in the system after validation.
     */
    public void editEntry(Entry entry) throws EntryValidationException {
        EntryValidator.validateEntry(entry);

        String result = entryPersistence.updateEntry(entry);
        if (result != null) {
            throw new EntryValidationException(result);
        }
    }

    /**
     * Retrieves pie chart data for entries within a given time range.
     *
     * @param user The user whose data is being retrieved
     * @param type The type of entries to retrieve (Income or Expense)
     * @return A list of PieEntry objects representing the data
     * @throws EntryValidationException If no data is available for visualization
     */
    public ArrayList<PieEntry> getPieChartDataWeek(User user, String type) throws EntryValidationException {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> pieEntriesMap = null;
        if (type.equals(StringConfig.EXPENSE_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataExpenseWeek(user.getUserID());
        } else if (type.equals(StringConfig.INCOME_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataIncomeWeek(user.getUserID());
        }

        if (pieEntriesMap != null) {
            for (String key : pieEntriesMap.keySet()) {
                Float value = pieEntriesMap.get(key);
                if ( value != null)
                    pieEntries.add(new PieEntry(value , key));
                }
        } else {
            throw new EntryValidationException(StringConfig.CHART_DATA_NULL_ERROR);

        }

        return pieEntries;
    }

    /**
     * Retrieves pie chart data for entries within a given time range.
     *
     * @param user The user whose data is being retrieved
     * @param type The type of entries to retrieve (Income or Expense)
     * @return A list of PieEntry objects representing the data
     * @throws EntryValidationException If no data is available for visualization
     */
    public ArrayList<PieEntry> getPieChartDataMonth(User user, String type) throws EntryValidationException {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> pieEntriesMap = null;
        if (type.equals(StringConfig.EXPENSE_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataExpenseMonth(user.getUserID());
        } else if (type.equals(StringConfig.INCOME_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataIncomeMonth(user.getUserID());
        }

        if (pieEntriesMap != null) {
            for (String key : pieEntriesMap.keySet()) {
                Float value = pieEntriesMap.get(key);
                if ( value != null)
                    pieEntries.add(new PieEntry(value , key));
            }
        } else {
            throw new EntryValidationException(StringConfig.CHART_DATA_NULL_ERROR);

        }

        return pieEntries;
    }


    /**
     * Retrieves pie chart data for entries within a given time range.
     *
     * @param user The user whose data is being retrieved
     * @param type The type of entries to retrieve (Income or Expense)
     * @return A list of PieEntry objects representing the data
     * @throws EntryValidationException If no data is available for visualization
     */
    public ArrayList<PieEntry> getPieChartDataYear(User user, String type) throws EntryValidationException {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> pieEntriesMap = null;
        if (type.equals(StringConfig.EXPENSE_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataExpenseFiveYear(user.getUserID());
        } else if (type.equals(StringConfig.INCOME_TYPE)) {
            pieEntriesMap = entryPersistence.getPieChartDataIncomeFiveYear(user.getUserID());
        }

        if (pieEntriesMap != null) {
            for (String key : pieEntriesMap.keySet()) {
                Float value = pieEntriesMap.get(key);
                if ( value != null)
                    pieEntries.add(new PieEntry(value , key));
            }
        } else {
            throw new EntryValidationException(StringConfig.CHART_DATA_NULL_ERROR);

        }

        return pieEntries;
    }


    /**
     * Retrieves summary pie chart data for income and expense comparison.
     *
     * @param user The user whose data is being retrieved
     * @param duration The time period to summarize (week, month, or year)
     * @return A list of PieEntry objects representing income and expense totals
     */
    public ArrayList<PieEntry> getPieChartDataSummary(User user, String duration) {
        float expenseTotal = 0;
        float incomeTotal = 0;

        if (duration.equals(StringConfig.DURATION_WEEK)) {
            incomeTotal = entryPersistence.getPieChartDataIncomeWeekTotal(user.getUserID());
            expenseTotal = entryPersistence.getPieChartDataExpenseWeekTotal(user.getUserID());
        }
        else if (duration.equals(StringConfig.DURATION_MONTH)) {
            incomeTotal = entryPersistence.getPieChartDataIncomeMonthTotal(user.getUserID());
            expenseTotal = entryPersistence.getPieChartDataExpenseMonthTotal(user.getUserID());
        }
        else if (duration.equals(StringConfig.DURATION_YEAR)) {
            incomeTotal = entryPersistence.getPieChartDataIncomeFiveYearTotal(user.getUserID());
            expenseTotal = entryPersistence.getPieChartDataExpenseFiveYearTotal(user.getUserID());
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(incomeTotal, StringConfig.INCOME_TYPE));
        pieEntries.add(new PieEntry(expenseTotal, StringConfig.EXPENSE_TYPE));
        return pieEntries;
    }

    /**
     * this method returns the category names for the given type
     *
     * @param type the type of category (income, expense, etc.)
     * @return list of category names
     */
    public static ArrayList<String> getCategoryNames(String type) {
        return EntryCategories.getCategoryNames(type);
    }

} // Entry Service Class