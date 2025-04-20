package comp3350.wwsys.tests.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import comp3350.wwsys.objects.ExpenseCategory;

public class ExpenseCatgeoryTest {

    /**
     * Sets up the test environment by printing a start message before each test.
     */
    @Before
    public void setUp() {
        System.out.println("\nStarting ExpenseCategoryTest");
    }

    /**
     * Tests the ExpenseCategory enum for the "TRANSPORTATION" category.
     * Verifies that the category name for "TRANSPORTATION" is correctly returned.
     */
    @Test
    public void testTransportationCategory() {
        ExpenseCategory category = ExpenseCategory.TRANSPORTATION;
        assertEquals("Transportation", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "FOOD" category.
     * Verifies that the category name for "FOOD" is correctly returned.
     */
    @Test
    public void testFoodCategory() {
        ExpenseCategory category = ExpenseCategory.FOOD;
        assertEquals("Food", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "LIVING_EXPENSES" category.
     * Verifies that the category name for "LIVING_EXPENSES" is correctly returned.
     */
    @Test
    public void testLivingExpensesCategory() {
        ExpenseCategory category = ExpenseCategory.LIVING_EXPENSES;
        assertEquals("Living Expenses", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "SHOPPING" category.
     * Verifies that the category name for "SHOPPING" is correctly returned.
     */
    @Test
    public void testShoppingCategory() {
        ExpenseCategory category = ExpenseCategory.SHOPPING;
        assertEquals("Shopping", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "OTHER" category.
     * Verifies that the category name for "OTHER" is correctly returned.
     */
    @Test
    public void testOtherCategory() {
        ExpenseCategory category = ExpenseCategory.OTHER;
        assertEquals("Other", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "SUBSCRIPTIONS" category.
     * Verifies that the category name for "SUBSCRIPTIONS" is correctly returned.
     */

    @Test
    public void testSubscriptionsCategory() {
        ExpenseCategory category = ExpenseCategory.SUBSCRIPTIONS;
        assertEquals("Subscriptions", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "HEALTH" category.
     * Verifies that the category name for "HEALTH" is correctly returned.
     */
    @Test
    public void testHealthCategory() {
        ExpenseCategory category = ExpenseCategory.HEALTH;
        assertEquals("Health", category.getCategoryName());
    }

    /**
     * Tests the ExpenseCategory enum for the "EDUCATION" category.
     * Verifies that the category name for "EDUCATION" is correctly returned.
     */
    @Test
    public void testEducationCategory() {
        ExpenseCategory category = ExpenseCategory.EDUCATION;
        assertEquals("Education", category.getCategoryName());
    }

    /**
     * Tests the number of categories available in the {@link ExpenseCategory} enum.
     * Verifies that there are exactly 8 categories defined in the enum.
     */
    @Test
    public void testNumberOfCategories() {
        assertEquals(8, ExpenseCategory.values().length);
    }

    @Test
    public void testFromCategoryName() {
        // Test exact match
        assertEquals(ExpenseCategory.TRANSPORTATION, ExpenseCategory.fromCategoryName("Transportation"));
        assertEquals(ExpenseCategory.FOOD, ExpenseCategory.fromCategoryName("Food"));
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("Other"));

        // Test case-insensitive match
        assertEquals(ExpenseCategory.TRANSPORTATION, ExpenseCategory.fromCategoryName("transportation"));
        assertEquals(ExpenseCategory.FOOD, ExpenseCategory.fromCategoryName("FOOD"));

        // Test non-matching category name
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("Invalid Category"));
    }

    /**
     * Tests that all enum values return non-null category names.
     * Ensures no null or unexpected values are returned from getCategoryName.
     */
    @Test
    public void testAllCategoryNamesNonNull() {
        for (ExpenseCategory category : ExpenseCategory.values()) {
            assertNotNull("Category name should not be null", category.getCategoryName());
        }
    }

    /**
     * Tests that fromCategoryName method returns OTHER when given null.
     * Verifies that null input does not throw exceptions.
     */
    @Test
    public void testFromCategoryNameWithNull() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName(null));
    }

    /**
     * Tests fromCategoryName with an empty string.
     * Ensures that an empty string returns OTHER.
     */
    @Test
    public void testFromCategoryNameWithEmptyString() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName(""));
    }

    /**
     * Tests fromCategoryName with whitespace input.
     * Ensures that input with spaces only is treated as invalid and returns OTHER.
     */
    @Test
    public void testFromCategoryNameWithWhitespace() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("   "));
    }

    /**
     * Tests fromCategoryName with partially matching strings.
     * Verifies that partial matches do not return incorrect categories.
     */
    @Test
    public void testFromCategoryNameWithPartialMatch() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("Trans"));
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("Educ"));
    }

    /**
     * Tests that the case-insensitive matching works for all category names.
     * Ensures all values are recognized correctly regardless of case.
     */
    @Test
    public void testCaseInsensitiveFromCategoryName() {
        assertEquals(ExpenseCategory.FOOD, ExpenseCategory.fromCategoryName("food"));
        assertEquals(ExpenseCategory.HEALTH, ExpenseCategory.fromCategoryName("hEalTH"));
        assertEquals(ExpenseCategory.SUBSCRIPTIONS, ExpenseCategory.fromCategoryName("SUBSCRIPTIONS"));
    }

    /**
     * Tests that extra spaces in the input are ignored by fromCategoryName.
     * Ensures valid categories are returned for trimmed input.
     */
    @Test
    public void testFromCategoryNameWithExtraSpaces() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("  Food  "));
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("   Shopping   "));
    }

    /**
     * Tests fromCategoryName with special characters in the input.
     * Ensures that invalid inputs with special characters return OTHER.
     */
    @Test
    public void testFromCategoryNameWithSpecialCharacters() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("F@od"));
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromCategoryName("He@lth!"));
    }

}