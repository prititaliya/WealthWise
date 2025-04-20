package comp3350.wwsys.tests.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.User;

public class EntryTest {
    private User user;
    private Entry expenseEntry;
    private Entry incomeEntry;
    private Entry entryWithEffectiveDate;
    private Entry fullEntry;

    @Before
    public void setUp() {
        user = new User("John", "Doe", "Prit1234@gmail.com", "Prit1234@");
        expenseEntry = new Entry(user.getUserID(), 200.0f, "Shopping", "Shopping", "expense", LocalDateTime.now());
        incomeEntry = new Entry(user.getUserID(), 300.0f, "Salary", "Full Time Job", "income", LocalDateTime.now());
        entryWithEffectiveDate = new Entry(user.getUserID(), 150.0f, "Groceries", "Food", "expense", LocalDateTime.of(2023, 10, 1, 12, 0));
        fullEntry = new Entry(UUID.randomUUID(), user.getUserID(), 500.0f, "Bonus", "Investments", "income", LocalDateTime.of(2023, 1, 1, 12, 0), LocalDateTime.of(2023, 1, 2, 12, 0));

    }

    /**
     * Tests the functionality of an expense entry object.
     * Verifies that the expense entry's attributes (amount, description, category, type, userId, etc.) are correct.
     */
        @Test
    public void testExpenseEntry() {
        assertEquals(200.0f, expenseEntry.getAmount(), 0.0);
        assertEquals("Shopping", expenseEntry.getDescription());
        assertEquals("Shopping", expenseEntry.getCategory());
        assertEquals(Entry.EntryType.EXPENSE, expenseEntry.getType());
        assertEquals(user.getUserID(), expenseEntry.getUserId());
        assertNotNull(expenseEntry.getId());
        assertNotNull(expenseEntry.getCreatedDate());
        assertNotNull(expenseEntry.getEffectiveDate());
    }

    /**
     * Tests the functionality of an income entry object.
     * Verifies that the income entry's attributes (amount, description, category, type, userId, etc.) are correct.
     */
    @Test
    public void testIncomeEntry() {
        assertEquals(300.0f, incomeEntry.getAmount(), 0.0);
        assertEquals("Salary", incomeEntry.getDescription());
        assertEquals("Full Time Job", incomeEntry.getCategory());
        assertEquals(Entry.EntryType.INCOME, incomeEntry.getType());
        assertEquals(user.getUserID(), incomeEntry.getUserId());
        assertNotNull(incomeEntry.getId());
        assertNotNull(incomeEntry.getCreatedDate());
        assertNotNull(incomeEntry.getEffectiveDate());
    }

    /**
     * Tests an entry object with a specified effective date.
     * Verifies that the effective date is correctly set and returned.
     */
    @Test
    public void testEntryWithEffectiveDate() {
        assertEquals(150.0f, entryWithEffectiveDate.getAmount(), 0.0);
        assertEquals("Groceries", entryWithEffectiveDate.getDescription());
        assertEquals("Food", entryWithEffectiveDate.getCategory());
        assertEquals(Entry.EntryType.EXPENSE, entryWithEffectiveDate.getType());
        assertEquals(user.getUserID(), entryWithEffectiveDate.getUserId());
        assertEquals(LocalDateTime.of(2023, 10, 1, 12, 0), entryWithEffectiveDate.getEffectiveDate());
    }

    /**
     * Tests a full entry object with both created and effective dates.
     * Verifies that both the created and effective dates are set correctly.
     */
    @Test
    public void testFullEntry() {
        assertEquals(500.0f, fullEntry.getAmount(), 0.0);
        assertEquals("Bonus", fullEntry.getDescription());
        assertEquals("Investments", fullEntry.getCategory());
        assertEquals(Entry.EntryType.INCOME, fullEntry.getType());
        assertEquals(user.getUserID(), fullEntry.getUserId());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), fullEntry.getCreatedDate());
        assertEquals(LocalDateTime.of(2023, 1, 2, 12, 0), fullEntry.getEffectiveDate());
    }

    /**
     * Tests the formatting of dates into strings.
     * Verifies that both the created and effective dates are correctly formatted as "yyyy-MM-dd HH:mm:ss".
     */
    @Test
    public void testDateFormats() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(LocalDateTime.now().format(formatter), expenseEntry.getCreatedDateString());
        assertEquals(LocalDateTime.now().format(formatter), expenseEntry.getEffectiveDateString());
    }

    /**
     * Tests the conversion of entry type from enum to string.
     * Verifies that the correct string representation ("expense" or "income") is returned for each entry type.
     */
    @Test
    public void testTypeString() {
        assertEquals("Expense", expenseEntry.getTypeString());
        assertEquals("Income", incomeEntry.getTypeString());
    }

    /**
     * Tests the behavior of the Entry class when a negative amount is provided.
     * Verifies that the negative amount is correctly stored and returned by
     * the getAmount method.
     */
    @Test
    public void testNegativeAmount() {
        Entry entry = new Entry(user.getUserID(), -100.0f, "Refund", "Expense", "expense", LocalDateTime.now());
        assertEquals(-100.0f, entry.getAmount(), 0.0);
    }

    /**
     * Tests the behavior of the Entry class when an empty string is provided
     * for the description field. Verifies that the empty string is correctly
     * stored and returned by the getDescription method.
     */
    @Test
    public void testEmptyDescription() {
        Entry entry = new Entry(user.getUserID(), 50.0f, "", "Misc", "income", LocalDateTime.now());
        assertEquals("", entry.getDescription());
    }

    /**
     * Tests the behavior of the Entry class when null values are provided
     * for various parameters in the constructor. Verifies that nullable fields
     * are set to null and numeric fields are initialized correctly.
     */
    @Test
    public void testNullValuesInConstructor() {
        Entry entry = new Entry(null, 0.0f, null, null, null, LocalDateTime.now());
        assertNull(entry.getType()); // Now null type is handled
        assertNull(entry.getCategory());
        assertNull(entry.getDescription());
        assertEquals(0.0f, entry.getAmount(), 0.0);
    }

    /**
     * Tests the behavior of the Entry class when an invalid type is provided.
     * Verifies that the type field is set to null when the provided type does
     * not match expected values ("income" or "expense").
     */
    @Test
    public void testInvalidType() {
        Entry entry = new Entry(user.getUserID(), 100.0f, "Invalid Type", "Misc", "unknown", LocalDateTime.now());
        assertNull(entry.getType()); // Assuming the type does not default
    }

    /**
     * Tests the behavior of the Entry class when a future effective date is provided.
     * Verifies that the effective date is correctly set and matches the provided
     * future date.
     */
    @Test
    public void testFutureEffectiveDate() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(30);
        Entry entry = new Entry(user.getUserID(), 250.0f, "Future Investment", "Finance", "income", futureDate);
        assertEquals(futureDate, entry.getEffectiveDate());
    }
}