package comp3350.wwsys.persistence.stub;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.persistence.EntryPersistence;

/**
 * EntryPersistebceStub
 * Class to simulate the EntryPersistence
 */
public class EntryPersistenceStub implements EntryPersistence {
    // List of entries
    private final List<Entry> entries;

    /**
     * Constructor for EntryPersistenceStub
     * adds some entries to the list
     */
    public EntryPersistenceStub() {
        entries = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();
        entries.add(new Entry(UUID.randomUUID(), 100.0f, "wallmart shopping", "grocery", "expense", date));
        entries.add(new Entry(UUID.randomUUID(), 200.0f, "movie night", "entertainment", "income", date));
        entries.add(new Entry(UUID.randomUUID(), 300.0f, "paycheck", "salary", "income", date));
        entries.add(new Entry(UUID.randomUUID(), 400.0f, "rent", "housing", "expense", date.withDayOfMonth(1)));
        entries.add(new Entry(UUID.randomUUID(), 500.0f, "car payment", "transportation", "expense", date.withDayOfMonth(1)));
        entries.add(new Entry(UUID.randomUUID(), 600.0f, "restaurant", "food", "expense", date));
        entries.add(new Entry(UUID.randomUUID(), 700.0f, "birthday gift", "gift", "expense", date));
        entries.add(new Entry(UUID.randomUUID(), 800.0f, "dividends", "dividends", "income", date));
        entries.add(new Entry(UUID.randomUUID(), 900.0f, "interest", "interest", "income", date));
    }





    /**
     * Method to insert an entry
     *
     * @param currentEntry Entry to be inserted
     * @return Entry inserted
     */
    @Override
    public String insertEntry(Entry currentEntry) {
        String result = "Cannot add Entry";
        if (currentEntry != null) {
            entries.add(currentEntry);
            result = null;
        }
        return result;
    }

    /**
     * Method to update an entry
     *
     * @param currentEntry Entry to be updated
     * @return Entry updated
     */
    @Override
    public String updateEntry(Entry currentEntry) {
        String result = "Cannot update Entry";
        if (currentEntry != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getId().equals(currentEntry.getId())) {
                    entries.set(i, currentEntry);
                    result = null;
                }
            }
        }
        return result;
    }

    /**
     * Method to delete an entry
     *
     * @param currentEntry Entry to be deleted
     * @return boolean if the entry was deleted
     */
    @Override
    public boolean deleteEntry(Entry currentEntry) {
        boolean result = false;
        if (currentEntry != null) {
            int i = 0;
            while (i < entries.size() && !result) {
                if (entries.get(i).getId().equals(currentEntry.getId())) {
                    entries.remove(i);
                    result = true;
                }
                i++;
            }
        }
        return result;
    }

    /**
     * Retrieves the total amount of a specific type (income/expense) for a given user in the current month.
     *
     * @param userID the unique identifier of the user
     * @param type   the type of entry ("income" or "expense")
     * @return the total amount for the specified type and user
     */
    @Override
    public float getMonthTotal(UUID userID, String type) {
        float result = 0.00f;
        Entry.EntryType entryType = null;

        if ("income".equals(type)) {
            entryType = Entry.EntryType.INCOME;
        } else if ("expense".equals(type)) {
            entryType = Entry.EntryType.EXPENSE;
        }

        if (entryType != null) {
            for (Entry entry : entries) {
                if (entry.getType().equals(entryType) && userID.equals(entry.getUserId())) {
                    result += entry.getAmount();
                }
            }
        }
        return result;
    }


    /**
     * Retrieves a list of all expense entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @return a list of expense entries for the user
     */
    @Override
    public ArrayList<Entry> userExpenseEntriesObject(UUID userID) {
        ArrayList<Entry> returnEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.EXPENSE) && entry.getUserId().equals(userID)) {
                returnEntries.add(entry);
            }
        }
        return returnEntries;
    }

    /**
     * Retrieves a list of all income entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @return a list of income entries for the user
     */
    @Override
    public ArrayList<Entry> userIncomeEntriesObject(UUID userID) {
        ArrayList<Entry> returnEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.INCOME) && entry.getUserId().equals(userID)) {
                returnEntries.add(entry);
            }
        }
        return returnEntries;
    }

    @Override
    public ArrayList<Entry> getPast5yearTotalIncome(UUID userID) {
        LocalDateTime[] time = FiveYearHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.INCOME) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    @Override
    public ArrayList<Entry> getPast5yearTotalExpense(UUID userID) {

        LocalDateTime[] time = FiveYearHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    @Override
    public ArrayList<Entry> getPastWeekEntryTotalIncome(UUID userID) {
        LocalDateTime[] time = weekHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.INCOME) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    @Override
        public ArrayList<Entry> getPastWeekEntryTotalExpense(UUID userID) {
        LocalDateTime[] time = weekHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    @Override
    public ArrayList<Entry> getPastYearEntryTotalIncome(UUID userID) {
        LocalDateTime[] time = monthHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.INCOME) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    @Override
    public ArrayList<Entry> getPastYearEntryTotalExpense(UUID userID) {

        LocalDateTime[] time = monthHelper();
        ArrayList<Entry> toReturn = new ArrayList<>();
        for(Entry entry : entries) {
            if (entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(time[0], entry.getEffectiveDate(), time[1])) {
                toReturn.add(entry);
            }
        }
        return toReturn;
    }

    /**
     * Retrieves a limited number of income entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @param limit  the maximum number of entries to retrieve
     * @return a list of the most recent income entries, limited by the specified count
     */
    @Override
    public ArrayList<Entry> userIncomeEntriesObject(UUID userID, int limit) {

        ArrayList<Entry> returnEntries = new ArrayList<>();
        int i = 0;
        int added = 0;
        while (i < entries.size() && added < limit) {
            Entry entry = entries.get(i);
            if (entry.getType().equals(Entry.EntryType.INCOME) && entry.getUserId().equals(userID)) {
                returnEntries.add(entry);
                added++;
            }
            i++;
        }
        return returnEntries;
    }

    /**
     * Retrieves a limited number of expense entries for a specific user.
     *
     * @param userID the unique identifier of the user
     * @param limit  the maximum number of entries to retrieve
     * @return a list of the most recent expense entries, limited by the specified count
     */
    @Override
    public ArrayList<Entry> userExpenseEntriesObject(UUID userID, int limit) {

        ArrayList<Entry> returnEntries = new ArrayList<>();
        int i = 0;
        int added = 0;
        while (i < entries.size() && added < limit) {
            Entry entry = entries.get(i);
            if (entry.getType().equals(Entry.EntryType.EXPENSE) && entry.getUserId().equals(userID)) {
                returnEntries.add(entry);
                added++;
            }
            i++;
        }
        return returnEntries;
    }

    @Override
    public Map<String, Float> getPieChartDataExpenseWeek(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time = weekHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1]) && entry.getType().equals(Entry.EntryType.EXPENSE)) {

                if (!entriesMap.containsKey(entry.getCategory())) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    @Override
    public Map<String, Float> getPieChartDataIncomeWeek(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time =weekHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1]) && entry.getType().equals(Entry.EntryType.INCOME) ) {

                if (!entriesMap.containsKey(entry.getCategory())) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    @Override
    public float getPieChartDataExpenseWeekTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] week = weekHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(week[0],entry.getEffectiveDate(), week[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }

    @Override
    public float getPieChartDataIncomeWeekTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] week = weekHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.INCOME) && inBetween(week[0],entry.getEffectiveDate(), week[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }

    @Override
    public float getPieChartDataExpenseMonthTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] month = monthHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(month[0],entry.getEffectiveDate(), month[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }

    @Override
    public float getPieChartDataIncomeMonthTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] month = monthHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.INCOME) && inBetween(month[0],entry.getEffectiveDate(), month[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }

    @Override
    public float getPieChartDataExpenseFiveYearTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] year = FiveYearHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.EXPENSE) && inBetween(year[0],entry.getEffectiveDate(),year[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }

    @Override
    public float getPieChartDataIncomeFiveYearTotal(UUID userID) {
        float total = 0;
        LocalDateTime[] year = FiveYearHelper();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if ( entry.getType().equals(Entry.EntryType.INCOME) && inBetween(year[0],entry.getEffectiveDate(),year[1])) {
                total += entry.getAmount();
            }
        }
        return total;
    }


    @Override
    public Map<String, Float> getPieChartDataExpenseMonth(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time = monthHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1])&& entry.getType().equals(Entry.EntryType.EXPENSE)) {

                if (!entriesMap.containsKey(entry.getCategory()) ) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    @Override
    public Map<String, Float> getPieChartDataIncomeMonth(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time = monthHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1])&& entry.getType().equals(Entry.EntryType.INCOME)) {

                if (!entriesMap.containsKey(entry.getCategory()) ) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    @Override
    public Map<String, Float> getPieChartDataIncomeFiveYear(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time = FiveYearHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1])&& entry.getType().equals(Entry.EntryType.INCOME)) {

                if (!entriesMap.containsKey(entry.getCategory()) ) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    @Override
    public Map<String, Float> getPieChartDataExpenseFiveYear(UUID userID) {
        Map<String, Float> entriesMap = new HashMap<>();
        LocalDateTime[] time = FiveYearHelper();

        for (Entry entry: entries) {
            if (inBetween(time[0], entry.getEffectiveDate(), time[1])&& entry.getType().equals(Entry.EntryType.EXPENSE)) {

                if (!entriesMap.containsKey(entry.getCategory()) ) {
                    entriesMap.put(entry.getCategory(), entry.getAmount());
                } else {
                    Float val = entriesMap.get(entry.getCategory());
                    entriesMap.put(entry.getCategory(), val + entry.getAmount());
                }
            }
        }
        return entriesMap;
    }

    private LocalDateTime[] weekHelper() {
        return new LocalDateTime[]{LocalDateTime.now().minusWeeks(1).plusDays(1).toLocalDate().atStartOfDay(), LocalDateTime.now().with(LocalTime.MAX)};
    }

    private LocalDateTime[] monthHelper() {
        return new LocalDateTime[]{LocalDateTime.now().minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN), LocalDateTime.now().with(LocalTime.MAX),LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX)};
    }

    private LocalDateTime[] FiveYearHelper() {
        return new LocalDateTime[]{LocalDateTime.now().minusYears(5).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN), LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX)};
    }

    private boolean inBetween (LocalDateTime before, LocalDateTime middle, LocalDateTime after) {
        return before.isBefore(middle) && after.isAfter(middle);
    }



}