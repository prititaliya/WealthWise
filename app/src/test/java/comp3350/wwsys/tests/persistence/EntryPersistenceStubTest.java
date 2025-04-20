package comp3350.wwsys.tests.persistence;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.persistence.EntryPersistence;
import comp3350.wwsys.persistence.stub.EntryPersistenceStub;

public class EntryPersistenceStubTest {
    // variables
    private EntryPersistence entryPersistence;
    private Entry entry;
    private static UUID userId;

    /**
     * sets up the test environment before each test.
     * Initializes an EntryPersistenceStub and inserts a sample entry.
     */
    @Before
    public void setUp() {
        userId = UUID.randomUUID();
        entryPersistence = new EntryPersistenceStub();
        Entry entryTwo = new Entry(userId, 100.0f, "March Salary", "Salary", "Expense", LocalDateTime.now());
        entry = new Entry(userId, 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());
        entryPersistence.insertEntry(entry);
        entryPersistence.insertEntry(entryTwo);
    }

    /**
     * insertEntryTest
     * trying to add an entry to the stub database
     */
    @Test
    public void insertEntryTest() {
        assert (entryPersistence.insertEntry(entry) == null);
    }

    /**
     * trying to add null Entry to the stub database
     */
    @Test
    public void insertNullEntryTest() {
        assert (entryPersistence.insertEntry(null) != null);
    }

    /**
     * updateEntryTest
     * trying to update an entry in the stub database
     */
    @Test
    public void updateEntryTest() {
        Entry newEntry = new Entry(UUID.randomUUID(), entry.getId(), 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now(), LocalDateTime.of(2025, 3, 1, 0, 0));
        assert (entryPersistence.updateEntry(newEntry) != null);
    }


    /**
     * deleteEntryTest
     * trying to delete an entry from the stub database
     */

    @Test
    public void deleteEntryTest() {
        assertTrue(entryPersistence.deleteEntry(entry));
    }

    /**
     * trying to delete nullEntry from the stub database
     */
    @Test
    public void deleteNullEntryTest() {
        assertFalse(entryPersistence.deleteEntry(null));
    }

    /**
     * trying to delete middle entry from the stub database
     */
    @Test
    public void deleteMiddleEntryTest() {
        Entry entryOne = new Entry(UUID.randomUUID(), 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());
        Entry entryTwo = new Entry(UUID.randomUUID(), 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());
        Entry entryThree = new Entry(UUID.randomUUID(), 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());


        entryPersistence.insertEntry(entryTwo);
        entryPersistence.insertEntry(entryThree);
        assert (!entryPersistence.deleteEntry(entryOne));

    }

    /**
     * trying to delete an entry that does not exist in the stub database
     */
    @Test
    public void deleteNonExistentEntryTest() {
        Entry newEntry = new Entry(userId, 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());
        assertFalse(entryPersistence.deleteEntry(newEntry));
    }

    /**
     * trying to get the total monthly income for a user
     */
    @Test
    public void getMonthlyIncomeTest() {
        assert (entryPersistence.getMonthTotal(userId, "income") == 100.0f);
    }

    /**
     * trying to get the total monthly expense for a user
     */
    @Test
    public void getMonthlyExpenseTest() {
        assert (entryPersistence.getMonthTotal(userId, "expense") == 100.0f);
    }

    /**
     * trying to get the total monthly expense for a user
     */
    @Test
    public void getMonthlyNulleTest2() {
        assert (entryPersistence.getMonthTotal(userId, "bruh") == 0.00f);
    }

    /**
     * trying to get the total monthly income for a user that does not exist
     */
    @Test
    public void getMonthlyIncomeNonExistentUserTest() {
        assert (entryPersistence.getMonthTotal(UUID.randomUUID(), "income") == 0.0f);
    }

    /**
     * trying to get the total monthly expense for a user that does not exist
     */
    @Test
    public void getMonthlyExpenseNonExistentUserTest() {
        assert (entryPersistence.getMonthTotal(UUID.randomUUID(), "expense") == 0.0f);
    }

    /**
     * trying to get all entries for a user
     */
    @Test
    public void getEntriesTest() {
        assert (entryPersistence.userIncomeEntriesObject(userId).size() == 1);
    }

    /**
     * trying to get all entries for a user that does not exist
     */
    @Test
    public void getEntriesNonExistentUserTest() {
        assert (entryPersistence.userIncomeEntriesObject(UUID.randomUUID()).isEmpty());
    }

    /**
     * trying to get all entries for a user
     */
    @Test
    public void getEntriesExpenseTest() {
        assert (!entryPersistence.userExpenseEntriesObject(userId).isEmpty());
    }

    /**
     * trying to get all entries for a user that does not exist
     */
    @Test
    public void getEntriesExpenseNonExistentUserTest() {
        assert (entryPersistence.userExpenseEntriesObject(UUID.randomUUID()).isEmpty());
    }

    /**
     * userIncomeEntriesObjectTest with null user
     */
    @Test
    public void userIncomeEntriesObjectNullUserTest() {
        assert (entryPersistence.userIncomeEntriesObject(null).isEmpty());
    }

    /**
     * userExpenseEntriesObjectTest with null user
     */
    @Test
    public void userExpenseEntriesObjectNullUserTest() {
        assert (entryPersistence.userExpenseEntriesObject(null).isEmpty());
    }

    /**
     * userIncomeEntries with limit
     */
    @Test
    public void userIncomeEntriesObjectLimitTest() {
        assert (entryPersistence.userIncomeEntriesObject(userId, 1).size() == 1);
    }

    /**
     * userIncomeEntries with limit
     */
    @Test
    public void userIncomeEntriesObjectLimitTest2() {
        assert (entryPersistence.userIncomeEntriesObject(userId, 0).isEmpty());
    }

    /**
     * userIncomeEntries with limit
     */
    @Test
    public void userIncomeEntriesObjectLimitTest3() {
        assert (entryPersistence.userIncomeEntriesObject(userId, -1).isEmpty());
    }

    /**
     * userIncomeEntries with limmit
     */
    @Test
    public void userIncomeEntriesObjectLimitTest5() {
        assert (!entryPersistence.userIncomeEntriesObject(userId, 5).isEmpty());
    }

    /**
     * userExpenseEntries with limit
     */
    @Test
    public void userExpenseEntriesObjectLimitTest2() {
        assert (entryPersistence.userExpenseEntriesObject(userId, 0).isEmpty());
    }

    /**
     * userExpenseEntries with limit
     */
    @Test
    public void userExpenseEntriesObjectLimitTest3() {
        assert (entryPersistence.userExpenseEntriesObject(userId, -1).isEmpty());
    }

    /**
     * userExpenseEntries with limmit
     */
    @Test
    public void userExpenseEntriesObjectLimitTest4() {
        assert (!entryPersistence.userExpenseEntriesObject(userId, 5).isEmpty());
    }

    /**
     * userExpense with some data
     */
    @Test
    public void userExpenseEntriesObjectTest() {
        Entry newEntry = new Entry(userId, 100.0f, "March Salary", "Salary", "Expense", LocalDateTime.now());
        entryPersistence.insertEntry(newEntry);
        assert (entryPersistence.userExpenseEntriesObject(userId).size() == 2);
    }

    /**
     * userIncome with some data with limit
     */
    @Test
    public void userIncomeEntriesObjectLimitTest4() {
        Entry newEntry = new Entry(userId, 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now());
        entryPersistence.insertEntry(newEntry);
        assert (entryPersistence.userIncomeEntriesObject(userId, 1).size() == 1);
    }

    /**
     * updating with multiple entries
     */
    @Test
    public void updateEntryMultipleTest() {
        Entry newEntry = new Entry(UUID.randomUUID(), userId, 100.0f, "March Salary", "Salary", "Income", LocalDateTime.now(), LocalDateTime.of(2025, 3, 1, 0, 0));
        Entry newEntry1 = new Entry(UUID.randomUUID(), userId, 1600.0f, "March Salary", "Salary", "Income", LocalDateTime.now(), LocalDateTime.of(2025, 3, 1, 0, 0));
        entryPersistence.insertEntry(newEntry);
        assert (entryPersistence.updateEntry(null) != null);
        assert (entryPersistence.updateEntry(newEntry1) != null);
        assert (entryPersistence.updateEntry(newEntry) == null);
    }

    @Test
    public void testgetPast5yearTotalIncome() {
        assertEquals(5, entryPersistence.getPast5yearTotalIncome(userId).size());
    }

    @Test
    public void testgetPast5yearTotalExpense() {
        assertEquals(6, entryPersistence.getPast5yearTotalExpense(userId).size());
    }

    @Test
    public void testgetPast5yearTotalIncomeNullUser() {
        assertEquals(5, entryPersistence.getPast5yearTotalIncome(null).size());
    }


    @Test
    public void testgetPastWeekEntryTotalIncome() {
        assertEquals(5, entryPersistence.getPastWeekEntryTotalIncome(userId).size());
    }

    @Test
    public void testgetPastWeekEntryTotalExpense() {
        assertEquals(4, entryPersistence.getPastWeekEntryTotalExpense(userId).size());
    }

    @Test
    public void testgetPastYearEntryTotalIncome(){
        assertEquals(5, entryPersistence.getPastYearEntryTotalIncome(userId).size());
    }
    @Test
    public void testgetPastYearEntryTotalExpense(){
        assertEquals(6, entryPersistence.getPastYearEntryTotalExpense(userId).size());
    }

    @Test
    public void testgetPieChartDataExpenseWeek(){
        assertNotNull(entryPersistence.getPieChartDataExpenseWeek(userId));
    }
    @Test
    public void testgetPieChartDataIncomeWeek(){
        assertNotNull(entryPersistence.getPieChartDataIncomeWeek(userId));
    }

    @Test
    public void testgetPieChartDataExpenseWeekTotal(){
        assertEquals(1500.0, entryPersistence.getPieChartDataExpenseWeekTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataIncomeWeekTotal(){
        assertEquals(2300.0, entryPersistence.getPieChartDataIncomeWeekTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataExpenseMonthTotal(){
        assertEquals(2400.0, entryPersistence.getPieChartDataExpenseMonthTotal(userId), 0);
    }
    @Test
    public void testgetPieChartDataExpenseFiveYearTotal(){
        assertEquals(2400.0, entryPersistence.getPieChartDataExpenseFiveYearTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataIncomeMonthTotal(){
        assertEquals(2300.0, entryPersistence.getPieChartDataIncomeMonthTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataExpenseMonth(){
        assertNotNull(entryPersistence.getPieChartDataExpenseMonth(userId));
    }

    @Test
    public void testgetPieChartDataIncomeMonth(){
        assertNotNull(entryPersistence.getPieChartDataIncomeMonth(userId));
    }

    @Test
    public void testgetPieChartDataIncomeFiveYear(){
        assertEquals(2300.0, entryPersistence.getPieChartDataIncomeFiveYearTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataExpenseFiveYear(){
        assertEquals(2400.0, entryPersistence.getPieChartDataExpenseFiveYearTotal(userId), 0);
    }

    @Test
    public void testgetPieChartDataIncomeMonth1(){
        assertNotNull(entryPersistence.getPieChartDataIncomeMonth(userId));
    }

    @Test
    public void testgetPieChartDataIncomeFiveYear1(){
        assertNotNull(entryPersistence.getPieChartDataIncomeFiveYear(userId));
    }

    @Test
    public void testgetPieChartDataExpenseFiveYear1(){
        assertNotNull(entryPersistence.getPieChartDataExpenseFiveYear(userId));
    }



}
