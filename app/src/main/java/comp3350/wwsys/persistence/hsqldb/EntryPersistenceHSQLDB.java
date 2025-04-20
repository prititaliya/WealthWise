package comp3350.wwsys.persistence.hsqldb;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import comp3350.wwsys.objects.Entry;

import comp3350.wwsys.persistence.EntryPersistence;

/**
 * This class implements UserPersistence using an HSQLDB database.
 * It provides methods to insert, update, delete, and fetch users
 * from the database.
 */
public class EntryPersistenceHSQLDB extends PersistenceHSQLB implements EntryPersistence {

    /**
     * Constructor.
     *
     * @param dbPath the path to the HSQLDB database file
     */
    public EntryPersistenceHSQLDB(final String dbPath) {
        super(dbPath);
    }

    /**
     * Retrieves the total income for the current month for a specific user.
     *
     * @param userID The UUID of the user whose monthly income is to be calculated.
     * @return The total income for the current month. Returns 0 if no income entries are found.
     */
    @Override
    public float getMonthTotal(UUID userID, String type) {
        float result = 0;
        int currentMonth = LocalDateTime.now().getMonthValue();
        String sql = "SELECT SUM(amount) AS total FROM Entry WHERE TYPE = ? AND USER_ID = ? AND MONTH(EFFECTIVE_DATE) = ?";

        try (Connection conn = connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, userID.toString());
            pstmt.setInt(3, currentMonth);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getFloat("total");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }



    /**
     * Retrieves all income entries (as Entry objects) for a given user.
     *
     * @param userID the UUID of the user
     * @return an ArrayList of Entry objects with type 'INCOME'
     */
    @Override
    public ArrayList<Entry> userIncomeEntriesObject(UUID userID) {
        String sql = "SELECT * FROM Entry WHERE TYPE = 'Income' AND USER_ID = ? ORDER BY EFFECTIVE_DATE DESC";
        String[] values = {userID.toString()};

        return getEntries(sql, values);
    }

    @Override
    public ArrayList<Entry> getPast5yearTotalIncome(UUID userID)  {
            return getPastNumYearEntryTotal(userID, "Income", 5);
    }

    @Override
    public ArrayList<Entry> getPast5yearTotalExpense(UUID userID)  {
        return getPastNumYearEntryTotal(userID, "Expense", 5);
    }

    @Override
    public ArrayList<Entry> getPastYearEntryTotalIncome(UUID userID) {
        return getPastNumYearEntryTotal(userID, "Income", 1);
    }

    @Override
    public ArrayList<Entry> getPastYearEntryTotalExpense(UUID userID) {
        return getPastNumYearEntryTotal(userID, "Expense", 1);
    }

    @Override
    public ArrayList<Entry> getPastWeekEntryTotalIncome(UUID userID) {
        return getPastWeekEntryTotal(userID, "Income");
    }

    @Override
    public ArrayList<Entry> getPastWeekEntryTotalExpense(UUID userID) {
        return getPastWeekEntryTotal(userID, "Expense");
    }

    private ArrayList<Entry> getPastWeekEntryTotal(UUID userID, String type) {

        LocalDateTime[] week = weekHelper();

        String sql = "SELECT * FROM Entry WHERE TYPE = ? AND USER_ID = ? AND EFFECTIVE_DATE BETWEEN ? AND ? ORDER BY EFFECTIVE_DATE ASC";
        String[] values = {type, userID.toString(), timestampStringHelper(week[0]), timestampStringHelper(week[1]) };

        return getEntries(sql,values);
    }



    private ArrayList<Entry> getPastNumYearEntryTotal(UUID userID, String type, int num) {

        LocalDateTime start = LocalDateTime.now().minusYears(num).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        String sql = "SELECT * FROM Entry WHERE TYPE = ? AND USER_ID = ? AND EFFECTIVE_DATE BETWEEN ? AND ? ORDER BY EFFECTIVE_DATE ASC";
        String[] values = {type, userID.toString(), timestampStringHelper(start), timestampStringHelper(end) };

        return getEntries(sql,values);
    }


    /**
     * Retrieves all income entries (as Entry objects) for a given user.
     *
     * @param userID the UUID of the user
     * @return an ArrayList of Entry objects with type 'INCOME'
     */
    @Override
    public ArrayList<Entry> userIncomeEntriesObject(UUID userID, int limit) {
        String sql = "SELECT * FROM Entry WHERE TYPE = 'Income' AND USER_ID = ? ORDER BY EFFECTIVE_DATE DESC LIMIT ?";
        String[] values = {userID.toString() , Integer.toString(limit)};

        return getEntries(sql, values);
    }

    /**
     * Retrieves all expense entries (as Entry objects) for a given user.
     *
     * @param userID the UUID of the user
     * @return an ArrayList of Entry objects with type 'EXPENSE'
     */
    @Override
    public ArrayList<Entry> userExpenseEntriesObject(UUID userID) {
        String sql = "SELECT * FROM Entry WHERE TYPE = 'Expense' AND USER_ID = ? ORDER BY EFFECTIVE_DATE DESC";
        String[] values = {userID.toString()};

        return getEntries(sql, values);
    }

    /**
     * Retrieves all expense entries (as Entry objects) for a given user.
     *
     * @param userID the UUID of the user
     * @return an ArrayList of Entry objects with type 'EXPENSE'
     */
    @Override
    public ArrayList<Entry> userExpenseEntriesObject(UUID userID, int count) {
        String sql = "SELECT * FROM Entry WHERE TYPE = 'Expense' AND USER_ID = ? ORDER BY EFFECTIVE_DATE DESC LIMIT ?";
        String[] values = {userID.toString(), Integer.toString(count)};

        return getEntries(sql, values);
    }



    /**
     * Inserts a new Entry into the database.
     *
     * @param currentEntry the Entry object to be inserted
     * @return null if the insertion is successful; otherwise, an error message
     */
    @Override
    public String insertEntry(Entry currentEntry) {
        String result = null;

        if (currentEntry != null) {
            String sql = "INSERT INTO PUBLIC.ENTRY VALUES(?,?,?,?,?,?,?,?)";
            String[] values = userValues(currentEntry);
            if (!updateTable(sql,values)) {
                result = "Cannot Add Entry To Database";
            }
        } else {
            result = "Current Entry is null";
        }
        return result;
    }

    /**
     * Updates an existing Entry in the database.
     *
     * @param currentEntry the Entry object with updated values
     * @return null if the update is successful; otherwise, an error message
     */
    @Override
    public String updateEntry(Entry currentEntry) {
        String result = null;

        if (currentEntry != null) {
            String sql = "UPDATE PUBLIC.ENTRY SET "
                    + "AMOUNT = ? ,"
                    + "DESCRIPTION = ? ,"
                    + "CATEGORY = ? ,"
                    + "TYPE = ? ,"
                    + "CREATED_DATE = ? ,"
                    + "EFFECTIVE_DATE = ? "
                    + "WHERE ID = ?";

            //I hate this but takes makes array the proper format to use
            String[] values = userValues(currentEntry);
            values = Arrays.copyOfRange(values, 2, values.length);
            values = Arrays.copyOf(values,values.length+1);
            values[values.length-1] = currentEntry.getId().toString();

            if (!updateTable(sql,values)) {
                result = "Cannot Add Entry To Database";
            }
        } else {
            result = "Current Entry is null";
        }
        return result;
    }

    /**
     * Deletes an Entry from the database.
     *
     * @param currentEntry the Entry object to be deleted
     * @return true if deletion is successful; false otherwise
     */
    @Override
    public boolean deleteEntry(Entry currentEntry) {
        String sql = "DELETE FROM PUBLIC.ENTRY WHERE ID = ?";
        String[] values = {currentEntry.getId().toString()};

        return updateTable(sql, values);
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

    /*******************************************
     *                                          *
     *       Income chart methods              *
     *                                          *
     ********************************************/
    @Override
    public Map<String, Float> getPieChartDataIncomeWeek(UUID userID) {
        LocalDateTime[] week = weekHelper();
        return getCategoryPieEntry(userID,week[0],week[1],"Income");
    }

    @Override
    public Map<String, Float> getPieChartDataIncomeMonth(UUID userID) {
        LocalDateTime[] month = monthHelper();
        return getCategoryPieEntry(userID,month[0],month[1],"Income");
    }

    @Override
    public float getPieChartDataIncomeWeekTotal(UUID userID) {
        LocalDateTime[] week = weekHelper();
        return getPieEntryTotal(userID, week[0],week[1],"Income");
    }


    @Override
    public float getPieChartDataIncomeMonthTotal(UUID userID) {
        LocalDateTime[] month = monthHelper();
        return getPieEntryTotal(userID, month[0], month[1],"Income");
    }

    @Override
    public float getPieChartDataIncomeFiveYearTotal(UUID userID) {
        LocalDateTime[] year = FiveYearHelper();
        return getPieEntryTotal(userID, year[0], year[1],"Income");
    }

    @Override
    public Map<String, Float> getPieChartDataIncomeFiveYear(UUID userID) {
        LocalDateTime[] year = FiveYearHelper();
        return getCategoryPieEntry(userID,year[0],year[1],"Income");
    }

    /*******************************************
    *                                          *
    *       Expense chart methods              *
    *                                          *
    ********************************************/
    @Override
    public Map<String, Float> getPieChartDataExpenseWeek(UUID userID) {
        LocalDateTime[] week = weekHelper();
        return getCategoryPieEntry(userID,week[0],week[1],"Expense");
    }


    @Override
    public float getPieChartDataExpenseWeekTotal(UUID userID) {
        LocalDateTime[] week = weekHelper();
        return getPieEntryTotal(userID, week[0],week[1],"Expense");
    }

    @Override
    public Map<String, Float> getPieChartDataExpenseMonth(UUID userID) {
        LocalDateTime[] month = monthHelper();
        return getCategoryPieEntry(userID,month[0],month[1],"Expense");
    }

    @Override
    public float getPieChartDataExpenseMonthTotal(UUID userID) {
        LocalDateTime[] month = monthHelper();
        return getPieEntryTotal(userID, month[0], month[1],"Expense");
    }

    @Override
    public Map<String, Float> getPieChartDataExpenseFiveYear(UUID userID) {
        LocalDateTime[] year = FiveYearHelper();
        return getCategoryPieEntry(userID,year[0],year[1],"Expense");
    }

    @Override
    public float getPieChartDataExpenseFiveYearTotal(UUID userID) {
        LocalDateTime[] year = FiveYearHelper();
        return getPieEntryTotal(userID, year[0], year[1],"Expense");
    }


    private float getPieEntryTotal(UUID userID, LocalDateTime start, LocalDateTime end, String type) {
        float total = 0;

        String sql = "SELECT SUM(amount) AS total " +
                "FROM Entry " +
                "WHERE TYPE = ? AND USER_ID = ? AND EFFECTIVE_DATE BETWEEN ? AND ?";

        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setString(2, userID.toString());
            statement.setString(3, timestampStringHelper(start));
            statement.setString(4, timestampStringHelper(end));
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                     total = resultSet.getFloat("total");
                }
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return total;
    }


    private Map<String, Float> getCategoryPieEntry(UUID userID, LocalDateTime start, LocalDateTime end, String type) {
        Map<String, Float> entries = new HashMap<>();

        String sql = "SELECT CATEGORY, SUM(amount) AS total " +
                "FROM Entry " +
                "WHERE TYPE = ? AND USER_ID = ? AND EFFECTIVE_DATE BETWEEN ? AND ? " +
                "GROUP BY CATEGORY ";

        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setString(2, userID.toString());
            statement.setString(3, timestampStringHelper(start));
            statement.setString(4, timestampStringHelper(end));

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String categoryType = resultSet.getString("CATEGORY");
                    float total = resultSet.getFloat("total");

                    entries.put(categoryType, total);
                }
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }

        // Return the list of PieEntries
        return entries;
    }




    /**
     * Helper method to execute a SQL query and convert the results into Entry objects.
     *
     * @param sql the SQL query to execute
     * @return an ArrayList of Entry objects built from the result set
     */
    private ArrayList<Entry> getEntries(String sql, String[] values) {
        ArrayList<Entry> entryList = new ArrayList<>();
        try (Connection conn = connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Entry entry = new Entry(
                            UUID.fromString(rs.getString("ID")),
                            UUID.fromString(rs.getString("USER_ID")),
                            rs.getFloat("AMOUNT"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("CATEGORY"),
                            rs.getString("TYPE"),
                            timestampHelper(rs.getTimestamp("CREATED_DATE")),
                            timestampHelper(rs.getTimestamp("EFFECTIVE_DATE"))
                    );
                    entryList.add(entry);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entryList;
    }

    /**
     * Extracts values from an Entry object into a String array.
     * The order is: ID, USER_ID, AMOUNT, DESCRIPTION, CATEGORY, TYPE, CREATED_DATE, EFFECTIVE_DATE.
     *
     * @param currentEntry the Entry object from which to extract values
     * @return a String array containing the values from the Entry
     */
    private String[] userValues(Entry currentEntry) {
        String[] values = null;
        if (currentEntry != null) {
            values = new String[] {
                    currentEntry.getId().toString(),
                    currentEntry.getUserId().toString(),
                    String.valueOf(currentEntry.getAmount()),
                    currentEntry.getDescription(),
                    currentEntry.getCategory(),
                    currentEntry.getTypeString(),
                    currentEntry.getCreatedDateString(),
                    currentEntry.getEffectiveDateString()
            };
        }
        return values;
    }

    /**
     * Helper method to convert a Timestamp object to a LocalDateTime.
     *
     * @param stamp the Timestamp to convert
     * @return the corresponding LocalDateTime
     */
    private LocalDateTime timestampHelper(Timestamp stamp) {
        return stamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Helper method to convert a LocalDateTime object to a String.
     *
     * @param stamp the Timestamp to convert
     * @return the corresponding LocalDateTime
     */
    private String timestampStringHelper(LocalDateTime stamp) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return stamp.format(formatter);
    }


}
