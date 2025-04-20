package comp3350.wwsys.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import comp3350.wwsys.business.EntryValidationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.hsqldb.EntryPersistenceHSQLDB;
import comp3350.wwsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.wwsys.tests.Utils.TestUtils;

public class EntryServiceIT {

    private UserService userService;
    private EntryService entryService;
    private User user,Bob;
    @Before
    public void setUp()  {

        try{
            File tempDB = TestUtils.copyDB();
            String path = tempDB.getAbsolutePath().replace(".script", "");

            userService = new UserService(new UserPersistenceHSQLDB(path));
            entryService = new EntryService(new EntryPersistenceHSQLDB(path));
            user = userService.getUserByEmail("prit@italiya.com");
            Bob = userService.getUserByEmail("bob@example.com");


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertNotNull(user);
        assertNotNull(Bob);
    }



    @Test
    public void testAddEntry2() {
        System.out.println("Testing add entry.");
        float amount = 100.0f;
        String description = "Test Entry";
        String category = "Test Category";
        String type = "wrongtype";
        assertThrows(EntryValidationException.class,()->entryService.addEntry(user, amount, description, category, type, LocalDateTime.now()));
    } @Test
    public void testAddEntry3() {
        System.out.println("Testing add entry.");


        assertThrows(Exception.class,()->entryService.addEntry(null, 0f, "","", "", LocalDateTime.now()));
    }
    @Test
    public void testAddEntry() {
        System.out.println("Testing add entry.");
        float amount = 100.0f;
        String description = "Test Entry";
        String category = "Test Category";
        String type = "iIcome";
        assertThrows(EntryValidationException.class,()->entryService.addEntry(user, amount, description, category, type, null));
    }
    @Test
    public void testGetMonthlyIncome() throws EntryValidationException {
        System.out.println("Testing get monthly income.");
        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,200, "","Allowance","Income",LocalDateTime.now());
        float monthlyTotal=entryService.getMonthlyIncome(user);
        assertEquals(4000f, monthlyTotal,0.01);
    }
    @Test
    public void testGetMonthlyExpense() throws EntryValidationException {
        System.out.println("Testing get Expense expense");
        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,210, "","Shopping","Expense",LocalDateTime.now());
        float monthlyTotal=entryService.getMonthlyExpense(user);
        assertEquals(210f, monthlyTotal,0.01);
    }
    @Test
    public void getMonthlIncomeForNUllUser(){
        System.out.println("Testing get monthly income for null user.");
        float monthlyTotal=entryService.getMonthlyIncome(null);
        assertEquals(0.0f, monthlyTotal,0.01);
    }
    @Test
    public void getMonthlExpenseForNUllUser(){
        System.out.println("Testing get monthly Expense for null user.");
        float monthlyTotal=entryService.getMonthlyExpense(null);
        assertEquals(0.0f, monthlyTotal,0.01);
    }

    @Test
    public void getMonthlyNet() throws EntryValidationException {
        System.out.println("Testing get monthly net.");


        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,10, "","Allowance","Expense",LocalDateTime.now());
        float monthlyNet=entryService.getMonthlyNet(user);
        assertEquals(3790f, monthlyNet,0.01);
    }
    @Test
    public void getMonthlyNet1() throws EntryValidationException {
        System.out.println("Testing get monthly net.");
        entryService.addEntry(user,3790, "","Allowance","Income",LocalDateTime.now());
        float monthlyNet=entryService.getMonthlyNet(user);
        assertEquals(3790f, monthlyNet,0.01);
    }
    @Test
    public void getMonthlyNetForNullUser(){
        System.out.println("Testing get monthly net for null user.");
        float monthlyNet=entryService.getMonthlyNet(null);
        assertEquals(0.0f, monthlyNet,0.01);
    }

    @Test
    public void getMonthlyIncomeLists(){
        System.out.println("Testing get monthly income lists.");
        try {
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            assertEquals(4,entryService.getIncomeEntries(user).size());
        } catch (EntryValidationException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getMonthlyIncomeLists1(){
        System.out.println("Testing get monthly income lists.");
        User user1;
        try {
            user1=userService.getUserByEmail("max@max.com");
        } catch (UserValidationException e) {
            throw new RuntimeException(e);
        }
            assertThrows(EntryValidationException.class,()->entryService.getIncomeEntries(user1));
    }
    @Test
    public void getMonthlyExpenseLists(){
        System.out.println("Testing get monthly expense lists.");
        try {
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            assertEquals(6,entryService.getExpenseEntries(user).size());
        } catch (EntryValidationException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getIncomeListForNoEntries(){
        System.out.println("Testing get monthly expense lists.");
        User user1;
        try {
             user1=userService.getUserByEmail("max@max.com");
        } catch (UserValidationException e) {
            throw new RuntimeException(e);
        }
            assertThrows(EntryValidationException.class,()->entryService.getExpenseEntries(user1));
    }

    @Test
    public void testForRecentEntries(){
        System.out.println("Testing get recent entries.");
        try {
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
            assertEquals(3,entryService.getMostRecentExpenseEntries(user,3).size());
        } catch (EntryValidationException e) {
            throw new RuntimeException(e);
        }

    } @Test
    public void testForRecentEntries1(){
        System.out.println("Testing get recent entries.");
        User user1;
        try {
            user1=userService.getUserByEmail("max@max.com");
        } catch (UserValidationException e) {
            throw new RuntimeException(e);
        }
            assertThrows(EntryValidationException.class,()->entryService.getMostRecentExpenseEntries(user1,3));


    }
    @Test
    public void testForRecentEntriesIncome(){
        System.out.println("Testing get recent entries.");
        try {
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
            entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());

            assertEquals(3,entryService.getMostRecentIncomeEntries(user,3).size());
        } catch (EntryValidationException e) {
            throw new RuntimeException(e);
        }

    } @Test
    public void testForRecentEntries1Income(){
        System.out.println("Testing get recent entries.");
        User user1;
        try {
            user1=userService.getUserByEmail("max@max.com");
        } catch (UserValidationException e) {
            throw new RuntimeException(e);
        }
            assertThrows(EntryValidationException.class,()->entryService.getMostRecentIncomeEntries(user1,3));
    }

    @Test
    public void testRemoveEntry(){
        System.out.println("Testing remove entry.");
        assertThrows(EntryValidationException.class,()->entryService.removeEntry(null));
    }
    @Test
    public void testRemoveEntry1(){
        System.out.println("Testing remove entry.");
        UUID entryID=UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime locDate = LocalDateTime.parse("2025-01-01 08:00:00", formatter);
        LocalDateTime effDate=LocalDateTime.parse("2025-03-01 08:30:00", formatter);
        Entry entry=new Entry(entryID,user.getUserID(), 1500.00f, "Salary Payment", "Salary", "Income", locDate, effDate);
        assertThrows(EntryValidationException.class,()->entryService.removeEntry(entry));
    }
    @Test
    public void testRemoveEntry2(){
        System.out.println("Testing remove entry.");
        UUID entryID=UUID.fromString("550e8400-e29b-41d4-a786-446655440000");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime locDate = LocalDateTime.parse("2025-01-01 08:00:00", formatter);
        LocalDateTime effDate=LocalDateTime.parse("2025-03-01 08:30:00", formatter);
        Entry entry=new Entry(entryID,user.getUserID(), 1500.00f, "Salary Payment", "Salary", "Income", locDate, effDate);
        assertThrows(EntryValidationException.class,()->entryService.removeEntry(entry));
    }
    @Test
    public void testEditEntry(){
        System.out.println("Testing edit entry.");
        UUID entryID=UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime locDate = LocalDateTime.parse("2025-01-01 08:00:00", formatter);
        LocalDateTime effDate=LocalDateTime.parse("2025-03-01 08:30:00", formatter);
        Entry entry=new Entry(entryID,user.getUserID(), 1500.00f, "Salary Payment", "Salary", "Expense", locDate, effDate);
        assertThrows(EntryValidationException.class,()->entryService.editEntry(entry));
    }

    @Test
    public void getCategoriesName(){
        System.out.println("Testing get categories name.");
        assertEquals(5,EntryService.getCategoryNames("Income").size());
        assertEquals(8,EntryService.getCategoryNames("Expense").size());
    }

    @After
    public void tearDown() {
        System.out.println("Cleaning up database.");
    }

    @Test
    public void testGetWeeklyData() throws EntryValidationException {
        System.out.println("Testing get weekly data.");

        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());

        assertDoesNotThrow(()->entryService.getWeeklyData(user,"Income"));
        assertDoesNotThrow(()->entryService.getWeeklyData(user,"Expense"));
        assertThrows(Exception.class,()->entryService.getWeeklyData(null,"Income"));

    }

    @Test
    public void testGetMonthlyData() throws EntryValidationException {
        System.out.println("Testing get 5 year total income.");
        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
        assertDoesNotThrow(()->entryService.getMonthlyData(user,"Income"));
        assertDoesNotThrow(()->entryService.getMonthlyData(user,"Expense"));
    }

    @Test
    public void testGetMonthlyData1(){
        System.out.println("Testing get 5 year total income.");
        assertThrows(Exception.class,()->entryService.getMonthlyData(null,"Income"));
        assertThrows(Exception.class,()->entryService.getMonthlyData(null,"Expense"));
    }

    @Test
    public void testGetYearData() throws EntryValidationException {
        System.out.println("Testing get 5 year total income.");
        entryService.addEntry(user,3800, "","Allowance","Income",LocalDateTime.now());
        entryService.addEntry(user,3800, "","Shopping","Expense",LocalDateTime.now());
        assertDoesNotThrow(()->entryService.getYearData(user,"Income"));
        assertDoesNotThrow(()->entryService.getYearData(user,"Expense"));
    }

    @Test
    public void testGetYearData1(){
        System.out.println("Testing get 5 year total income.");
        assertThrows(Exception.class,()->entryService.getYearData(null,"Income"));
        assertThrows(Exception.class,()->entryService.getYearData(null,"Expense"));
    }

    @Test
    public void testGetPieChartDataSummary(){
        System.out.println("Testing get pie chart data summary.");
        assertDoesNotThrow(()->entryService.getPieChartDataSummary(user,"week"));
        assertDoesNotThrow(()->entryService.getPieChartDataSummary(user,"month"));
        assertDoesNotThrow(()->entryService.getPieChartDataSummary(user,"year"));

    }


}
