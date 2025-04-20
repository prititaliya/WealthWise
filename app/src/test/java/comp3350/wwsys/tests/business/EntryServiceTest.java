package comp3350.wwsys.tests.business;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.objects.IncomeCategory;
import comp3350.wwsys.objects.ExpenseCategory;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.persistence.EntryPersistence;
import comp3350.wwsys.business.EntryValidator;

import static org.mockito.Mockito.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import comp3350.wwsys.business.EntryValidationException;


public class EntryServiceTest {
    @Mock
    private EntryPersistence mockEntryPersistence;
    private AutoCloseable closeable;
    private EntryService entryService;
    private User mockUser;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);// Initializes mock objects
        entryService = new EntryService(mockEntryPersistence);

        // Create a mock user with UUID
        mockUser = new User(
                "testFirstName",
                "testLastName",
                "testPassword",
                "testEmail@example.com"
        );
    }

    @After
    public void tearDown() throws Exception {
        // Close resources to avoid memory leaks
        closeable.close();
    }

    /**
     * Tests the constructor of the EntryService class.
     * Verifies that the EntryService instance is created successfully.
     */
    @Test
    public void testEntryServiceConstructor() {
        // Act
        EntryService entryService = new EntryService();

        // Assert
        assertNotNull(entryService); // Ensure the EntryService instance is created
    }

    /**
     * Tests the addEntry method for a valid entry scenario.
     * Verifies that the entry is validated and persisted successfully.
     */
    @Test
    public void testAddEntry_ValidEntry() throws Exception {
        // Mock static method validateEntry
        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            // Mock validateEntry to do nothing (simulate successful validation)
            mockedValidator.when(() -> EntryValidator.validateEntry(any(Entry.class))).thenAnswer(invocation -> null);

            // Mock the persistence layer to do nothing
            when(mockEntryPersistence.insertEntry(any(Entry.class))).thenReturn(null);

            // Act & Assert
            entryService.addEntry(
                    mockUser,
                    100.0f,
                    "Test Description",
                    "Salary",
                    "Income",
                    LocalDateTime.now()
            );

            // Verify that the static method was called
            mockedValidator.verify(() -> EntryValidator.validateEntry(any(Entry.class)));

            // Verify that the persistence layer was called
            verify(mockEntryPersistence, times(1)).insertEntry(any(Entry.class));
        }
    }

    /**
     * Tests the addEntry method for an invalid entry scenario.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testAddEntry_InvalidEntry() {
        // Mock static method validateEntry
        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            // Mock validateEntry to throw an exception (simulate validation failure)
            mockedValidator.when(() -> EntryValidator.validateEntry(any(Entry.class)))
                    .thenThrow(new EntryValidationException("Invalid entry details."));

            // Act & Assert
            Exception exception = assertThrows(
                    EntryValidationException.class,
                    () -> entryService.addEntry(
                            mockUser,
                            -100.0f, // Invalid amount
                            "",
                            "Salary",
                            "Income",
                            LocalDateTime.now()
                    )
            );

            // Assert exception message
            assertEquals("Invalid entry details.", exception.getMessage());

            // Verify that the static method was called
            mockedValidator.verify(() -> EntryValidator.validateEntry(any(Entry.class)));

            // Verify that the persistence layer was NOT called
            verify(mockEntryPersistence, never()).insertEntry(any(Entry.class));
        }
    }

    /**
     * Tests the addEntry method for a scenario where the amount is negative.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testAddEntry_NegativeAmount() {
        Exception exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.addEntry(
                        mockUser,
                        -500.0f, // Negative value
                        "Test Description",
                        "Salary",
                        "Income",
                        LocalDateTime.now()
                )
        );

        // Assert exception message
        assertEquals("Amount must be at least 0.01.", exception.getMessage());
    }

    /**
     * Tests the addEntry method for a scenario where the description is empty.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testAddEntry_EmptyDescription() {
        // Mock static method validateEntry
        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            // Mock validateEntry to throw an exception for empty description
            mockedValidator.when(() -> EntryValidator.validateEntry(any(Entry.class)))
                    .thenThrow(new EntryValidationException("Entry description cannot be empty."));

            // Mock the persistence layer to ensure it's never called
            when(mockEntryPersistence.insertEntry(any(Entry.class))).thenReturn(null);

            // Act & Assert
            Exception exception = assertThrows(
                    EntryValidationException.class,
                    () -> entryService.addEntry(
                            mockUser,
                            100.0f,
                            "",  // Empty description
                            "Salary",
                            "Income",
                            LocalDateTime.now()
                    )
            );

            // Assert exception message
            assertEquals("Entry description cannot be empty.", exception.getMessage());

            // Verify that the static method was called
            mockedValidator.verify(() -> EntryValidator.validateEntry(any(Entry.class)));

            // Verify that the persistence layer was NOT called
            verify(mockEntryPersistence, never()).insertEntry(any(Entry.class));
        }
    }


    /**
     * Tests the addEntry method to ensure it throws an exception when the insertEntry operation fails.
     * Verifies that the exception contains the appropriate error message.
     */
    @Test
    public void testAddEntryThrowsExceptionWhenInsertEntryFails() {
        // Arrange
        EntryPersistence mockEntryPersistence = Mockito.mock(EntryPersistence.class);
        EntryService entryService = new EntryService(mockEntryPersistence);
        String expectedErrorMessage = "Please select an entry type.";

        // Mocking entryPersistence to return a non-null result
        Mockito.when(mockEntryPersistence.insertEntry(Mockito.any(Entry.class)))
                .thenReturn(expectedErrorMessage);

        // Act & Assert
        EntryValidationException exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.addEntry(mockUser, 100.0f, "Test Description", "Test Category", "Test Type", LocalDateTime.now())
        );

        // Verify
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    /**
     * Tests the getMonthlyIncome method with a valid user.
     * Verifies the correct monthly income is retrieved and the persistence layer is called.
     */
    @Test
    public void testGetMonthlyIncome_ValidUser() {
        // Arrange
        when(mockEntryPersistence.getMonthTotal(mockUser.getUserID(), "Income")).thenReturn(1000.0f);

        // Act
        float monthlyIncome = entryService.getMonthlyIncome(mockUser);

        // Assert
        assertEquals(1000.0f, monthlyIncome, 0.001);
        verify(mockEntryPersistence, times(1)).getMonthTotal(mockUser.getUserID(), "Income");
    }

    /**
     * Tests the getMonthlyIncome method with a null user.
     * Verifies that the method returns 0.0 and does not call the persistence layer.
     */
    @Test
    public void testGetMonthlyIncome_NullUser() {
        // Act
        float monthlyIncome = entryService.getMonthlyIncome(null); // Passing null as user

        // Assert
        assertEquals(0.00f, monthlyIncome, 0.001);
    }

    /**
     * Tests the getMonthlyExpense method with a valid user.
     * Verifies the correct monthly expense is retrieved and the persistence layer is called.
     */
    @Test
    public void testGetMonthlyExpense_ValidUser() {
        // Arrange
        when(mockEntryPersistence.getMonthTotal(mockUser.getUserID(), "Expense")).thenReturn(500.0f);

        // Act
        float monthlyExpense = entryService.getMonthlyExpense(mockUser);

        // Assert
        assertEquals(500.0f, monthlyExpense, 0.001);
        verify(mockEntryPersistence, times(1)).getMonthTotal(mockUser.getUserID(), "Expense");
    }

    /**
     * Tests the getMonthlyExpense method with a null user.
     * Verifies that the method returns 0.0 and does not call the persistence layer.
     */
    @Test
    public void testGetMonthlyExpense_NullUser() {
        // Act
        float monthlyExpense = entryService.getMonthlyExpense(null); // Passing null as user

        // Assert
        assertEquals(0.00f, monthlyExpense, 0.001);
    }

    /**
     * Tests the getMostRecentIncomeEntries method with a valid user and a non-empty result.
     * Verifies the returned list matches the mock data and that the persistence layer is called.
     */
    @Test
    public void testGetMostRecentIncomeEntriesReturnsList() throws EntryValidationException {
        // Arrange
        EntryPersistence mockEntryPersistence = Mockito.mock(EntryPersistence.class);
        EntryService entryService = new EntryService(mockEntryPersistence);
        int count = 5;

        ArrayList<Entry> mockIncomeEntries = new ArrayList<>();
        mockIncomeEntries.add(new Entry(mockUser.getUserID(), 100.0f, "Description 1", "Category 1", "Income", LocalDateTime.now()));
        mockIncomeEntries.add(new Entry(mockUser.getUserID(), 200.0f, "Description 2", "Category 2", "Income", LocalDateTime.now()));

        // Mocking entryPersistence to return a non-empty list
        Mockito.when(mockEntryPersistence.userIncomeEntriesObject(mockUser.getUserID(), count))
                .thenReturn(mockIncomeEntries);

        // Act
        ArrayList<Entry> result = entryService.getMostRecentIncomeEntries(mockUser, count);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(mockIncomeEntries.size(), result.size());
        assertEquals(mockIncomeEntries, result); // Ensure the returned list matches the mock list
    }

    /**
     * Tests the getIncomeEntries method with a valid user and non-empty results.
     * Verifies that the returned entries match the mock data and the persistence layer is called.
     */
    @Test
    public void testGetIncomeEntries_ValidUser() throws Exception {
        // Arrange
        ArrayList<Entry> mockIncomeEntries = new ArrayList<>();
        mockIncomeEntries.add(new Entry(mockUser.getUserID(), 200.0f, "Income Description", "Salary", "Income", LocalDateTime.now()));

        when(mockEntryPersistence.userIncomeEntriesObject(mockUser.getUserID())).thenReturn(mockIncomeEntries);

        // Act
        ArrayList<Entry> incomeEntries = entryService.getIncomeEntries(mockUser);

        // Assert
        assertNotNull(incomeEntries);
        assertEquals(1, incomeEntries.size());
        verify(mockEntryPersistence, times(1)).userIncomeEntriesObject(mockUser.getUserID());
    }

    /**
     * Tests the getIncomeEntries method for a valid user with no entries.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testGetIncomeEntries_NoEntries() {
        // Arrange
        when(mockEntryPersistence.userIncomeEntriesObject(mockUser.getUserID())).thenReturn(new ArrayList<>()); // Empty list

        // Act & Assert
        Exception exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.getIncomeEntries(mockUser)
        );

        // Assert exception message
        assertEquals("No Income entries found.", exception.getMessage());

        // Verify that the persistence layer was called
        verify(mockEntryPersistence, times(1)).userIncomeEntriesObject(mockUser.getUserID());
    }



    /**
     * Tests the getMonthlyNet method with a valid user.
     * Verifies that the correct monthly net is calculated and the persistence layer is called.
     */
    @Test
    public void testGetMonthlyNet_ValidUser() {
        // Arrange
        when(mockEntryPersistence.getMonthTotal(mockUser.getUserID(), "Income")).thenReturn(1500.0f);
        when(mockEntryPersistence.getMonthTotal(mockUser.getUserID(), "Expense")).thenReturn(600.0f);

        // Act
        float monthlyNet = entryService.getMonthlyNet(mockUser);

        // Assert
        assertEquals(900.0f, monthlyNet, 0.001);
        verify(mockEntryPersistence, times(1)).getMonthTotal(mockUser.getUserID(), "Income");
        verify(mockEntryPersistence, times(1)).getMonthTotal(mockUser.getUserID(), "Expense");
    }

    /**
     * Tests the getMonthlyNet method with a null user.
     * Verifies that the method returns 0.0 and does not interact with the persistence layer.
     */
    @Test
    public void testGetMonthlyNet_NullUser() {
        // Act
        float monthlyNet = entryService.getMonthlyNet(null);

        // Assert
        assertEquals(0.00f, monthlyNet, 0.001);
    }

    /**
     * Tests the getExpenseEntries method for a valid user with existing expenses.
     * Verifies the returned entries match the mock data and the persistence layer is called.
     */
    @Test
    public void testGetExpenseEntries_ValidUser() throws Exception {
        // Arrange
        ArrayList<Entry> mockExpenseEntries = new ArrayList<>();
        mockExpenseEntries.add(new Entry(
                mockUser.getUserID(),
                150.0f,
                "Test Expense",
                "Food",
                "Expense",
                LocalDateTime.now()
        ));

        when(mockEntryPersistence.userExpenseEntriesObject(mockUser.getUserID())).thenReturn(mockExpenseEntries);

        // Act
        ArrayList<Entry> expenseEntries = entryService.getExpenseEntries(mockUser);

        // Assert
        assertNotNull(expenseEntries);
        assertEquals(1, expenseEntries.size());
        assertEquals("Test Expense", expenseEntries.get(0).getDescription());
        verify(mockEntryPersistence, times(1)).userExpenseEntriesObject(mockUser.getUserID());
    }

    /**
     * Tests the getExpenseEntries method for a valid user with no expenses.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testGetExpenseEntries_NoExpenses() {
        // Arrange
        when(mockEntryPersistence.userExpenseEntriesObject(mockUser.getUserID())).thenReturn(new ArrayList<>());

        // Act & Assert
        Exception exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.getExpenseEntries(mockUser)
        );

        // Assert
        assertEquals("No Expense entries found.", exception.getMessage());
        verify(mockEntryPersistence, times(1)).userExpenseEntriesObject(mockUser.getUserID());
    }

    /**
     * Tests the removeEntry method with a valid entry.
     * Verifies that the persistence layer deletes the entry and no exceptions are thrown.
     */
    @Test
    public void testRemoveEntry_Success() throws Exception {
        // Arrange
        Entry mockEntry = new Entry(
                mockUser.getUserID(),
                100.0f,
                "Test Description",
                "Category",
                "Expense",
                LocalDateTime.now()
        );

        when(mockEntryPersistence.deleteEntry(mockEntry)).thenReturn(true);

        // Act
        entryService.removeEntry(mockEntry);

        // Assert
        verify(mockEntryPersistence, times(1)).deleteEntry(mockEntry);
    }

    /**
     * Tests the removeEntry method for a null entry.
     * Verifies that an EntryValidationException is thrown with the appropriate error message.
     */
    @Test
    public void testRemoveEntry_NullEntry() {
        // Act & Assert
        Exception exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.removeEntry(null)
        );

        // Assert
        assertEquals("Unable to retrieve data.", exception.getMessage());
    }

    /**
     * Tests the removeEntry method when the deletion fails.
     * Verifies that an EntryValidationException is thrown with the correct error message.
     */
    @Test
    public void testRemoveEntry_DeletionFailed() {
        // Arrange
        Entry mockEntry = new Entry(
                mockUser.getUserID(),
                100.0f,
                "Test Description",
                "Category",
                "Expense",
                LocalDateTime.now()
        );

        when(mockEntryPersistence.deleteEntry(mockEntry)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(EntryValidationException.class, () -> entryService.removeEntry(mockEntry));

        // Assert
        assertEquals("Entry could not be deleted.", exception.getMessage());
        verify(mockEntryPersistence, times(1)).deleteEntry(mockEntry);
    }

    /**
     * Tests the editEntry method with a valid entry.
     * Verifies that the entry is validated and updated successfully.
     */
    @Test
    public void testEditEntry_Success() throws Exception {
        // Arrange
        Entry mockEntry = new Entry(
                mockUser.getUserID(),
                200.0f,
                "Updated Description",
                "Updated Category",
                "Income",
                LocalDateTime.now()
        );

        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            mockedValidator.when(() -> EntryValidator.validateEntry(mockEntry)).thenAnswer(invocation -> null);

            when(mockEntryPersistence.updateEntry(mockEntry)).thenReturn(null);

            // Act
            entryService.editEntry(mockEntry);

            // Assert
            mockedValidator.verify(() -> EntryValidator.validateEntry(mockEntry), times(1));
            verify(mockEntryPersistence, times(1)).updateEntry(mockEntry);
        }
    }

    /**
     * Tests the editEntry method when validation fails.
     * Verifies that an EntryValidationException is thrown and the persistence layer is not called.
     */
    @Test
    public void testEditEntry_ValidationFails() {
        // Arrange
        Entry mockEntry = new Entry(
                mockUser.getUserID(),
                200.0f,
                "Invalid Description",
                "Updated Category",
                "Expense",
                LocalDateTime.now()
        );

        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            mockedValidator.when(() -> EntryValidator.validateEntry(mockEntry))
                    .thenThrow(new EntryValidationException("Validation failed for the entry."));

            // Act & Assert
            Exception exception = assertThrows(EntryValidationException.class, () -> entryService.editEntry(mockEntry));

            // Assert
            assertEquals("Validation failed for the entry.", exception.getMessage());
            verify(mockEntryPersistence, times(0)).updateEntry(any(Entry.class));
        }
    }

    /**
     * Tests the editEntry method when the update operation fails.
     * Verifies that an EntryValidationException is thrown with the appropriate error message.
     */
    @Test
    public void testEditEntry_UpdateFails() {
        // Arrange
        Entry mockEntry = new Entry(
                mockUser.getUserID(),
                200.0f,
                "Updated Description",
                "Updated Category",
                "Expense",
                LocalDateTime.now()
        );

        // Mock static method validateEntry to do nothing
        try (MockedStatic<EntryValidator> mockedValidator = mockStatic(EntryValidator.class)) {
            mockedValidator.when(() -> EntryValidator.validateEntry(mockEntry)).thenAnswer(invocation -> null);

            // Mock the persistence layer to simulate a failed update
            when(mockEntryPersistence.updateEntry(mockEntry)).thenReturn("Entry could not be updated.");

            // Act & Assert
            Exception exception = assertThrows(EntryValidationException.class, () -> entryService.editEntry(mockEntry));

            // Verify exception message
            assertEquals("Entry could not be updated.", exception.getMessage());

            // Verify that validateEntry was called
            mockedValidator.verify(() -> EntryValidator.validateEntry(mockEntry), times(1));

            // Verify the persistence method was called
            verify(mockEntryPersistence, times(1)).updateEntry(mockEntry);
        }
    }

    /**
     * Tests the getMostRecentExpenseEntries method with a valid user.
     * Verifies the returned entries match the mock data.
     */
    @Test
    public void testGetMostRecentExpenseEntries_Success() throws EntryValidationException {
        // Arrange
        Entry entry1 = new Entry(
                mockUser.getUserID(),
                200.0f,
                "Invalid Description",
                "Updated Category",
                "Expense",
                LocalDateTime.now()
        );
        Entry entry2 = new Entry(
                mockUser.getUserID(),
                200.0f,
                "Invalid Description",
                "Updated Category",
                "Expense",
                LocalDateTime.now()
        );

        ArrayList<Entry> mockEntries = new ArrayList<>(Arrays.asList(entry1, entry2));
        Mockito.when(mockEntryPersistence.userExpenseEntriesObject(mockUser.getUserID(), 2))
                .thenReturn(mockEntries);

        // Act
        ArrayList<Entry> result = entryService.getMostRecentExpenseEntries(mockUser, 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * Tests the getMostRecentExpenseEntries method with a user who has no expense entries.
     * Verifies that an EntryValidationException is thrown with the appropriate message.
     */
    @Test
    public void testGetMostRecentExpenseEntries_NoExpenses() {
        // Arrange
        Mockito.when(mockEntryPersistence.userExpenseEntriesObject(mockUser.getUserID(), 2))
                .thenReturn(new ArrayList<>());

        // Act & Assert
        EntryValidationException exception = assertThrows(
                EntryValidationException.class,
                () -> entryService.getMostRecentExpenseEntries(mockUser, 2)
        );

        // Assert
        assertEquals("No Expense entries found.", exception.getMessage());
        Mockito.verify(mockEntryPersistence, Mockito.times(1))
                .userExpenseEntriesObject(mockUser.getUserID(), 2);
    }

    /**
     * Tests the getMostRecentExpenseEntries method when requesting more entries than are available.
     * Verifies that the returned entries match the available mock data.
     */
    @Test
    public void testGetMostRecentExpenseEntries_RequestMoreThanAvailable() throws Exception {
        // Arrange
        ArrayList<Entry> mockExpenseEntries = new ArrayList<>();
        mockExpenseEntries.add(new Entry(mockUser.getUserID(), 50.0f, "Expense 1", "Category", "Expense", LocalDateTime.now()));

        Mockito.when(mockEntryPersistence.userExpenseEntriesObject(mockUser.getUserID(), 5))
                .thenReturn(mockExpenseEntries);

        // Act
        ArrayList<Entry> recentExpenseEntries = entryService.getMostRecentExpenseEntries(mockUser, 5);

        // Assert
        assertNotNull(recentExpenseEntries);
        assertEquals(1, recentExpenseEntries.size());
        assertEquals("Expense 1", recentExpenseEntries.get(0).getDescription());
        Mockito.verify(mockEntryPersistence, Mockito.times(1))
                .userExpenseEntriesObject(mockUser.getUserID(), 5);
    }

    /**
     * Tests the getCategoryNames method for the "Income" category.
     * Verifies that all expected income category names are returned.
     */
    @Test
    public void testGetCategoryNames_ValidIncome() {
        // Arrange
        List<String> expectedCategories = new ArrayList<>();
        for (IncomeCategory category : IncomeCategory.values()) {
            expectedCategories.add(category.getCategoryName());
        }

        // Act
        ArrayList<String> actualCategories = EntryService.getCategoryNames("Income");

        // Assert
        assertNotNull(actualCategories);
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertTrue(actualCategories.containsAll(expectedCategories));
    }

    /**
     * Tests the getCategoryNames method for the "Expense" category.
     * Verifies that all expected expense category names are returned.
     */
    @Test
    public void testGetCategoryNames_ValidExpense() {
        // Arrange
        List<String> expectedCategories = new ArrayList<>();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            expectedCategories.add(category.getCategoryName());
        }

        // Act
        ArrayList<String> actualCategories = EntryService.getCategoryNames("Expense");

        // Assert
        assertNotNull(actualCategories);
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertTrue(actualCategories.containsAll(expectedCategories));
    }

    /**
     * Tests the getCategoryNames method with an invalid category type.
     * Verifies that the returned list is empty.
     */
    @Test
    public void testGetCategoryNames_InvalidType() {
        // Act
        ArrayList<String> actualCategories = EntryService.getCategoryNames("InvalidType");

        // Assert
        assertNotNull(actualCategories);
        assertTrue(actualCategories.isEmpty());
    }

}

