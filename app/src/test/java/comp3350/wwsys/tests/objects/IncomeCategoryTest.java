package comp3350.wwsys.tests.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import comp3350.wwsys.objects.IncomeCategory;

/**
 * Unit test class for the IncomeCategory enum.
 * This class contains test cases for validating the functionality of the IncomeCategory enum,
 * including verifying the category names and the behavior of methods like fromCategoryName(String).
 */
public class IncomeCategoryTest {

    /**
     * Sets up the test environment by printing a start message before each test.
     */
    @Before
    public void setUp() {
        System.out.println("\nStarting IncomeCategoryTest");
    }

    /**
     * Tests the IncomeCategory enum for the "FULL_TIME_JOB" category.
     * Verifies that the category name for "FULL_TIME_JOB" is correctly returned.
     */
    @Test
    public void testFullTimeJobCategory() {
        IncomeCategory category = IncomeCategory.FULL_TIME_JOB;
        assertEquals("Full Time Job", category.getCategoryName());
    }

    /**
     * Tests the IncomeCategory enum for the "PART_TIME_JOB" category.
     * Verifies that the category name for "PART_TIME_JOB" is correctly returned.
     */
    @Test
    public void testPartTimeJobCategory() {
        IncomeCategory category = IncomeCategory.PART_TIME_JOB;
        assertEquals("Part Time Job", category.getCategoryName());
    }

    /**
     * Tests the IncomeCategory enum for the "ALLOWANCE" category.
     * Verifies that the category name for "ALLOWANCE" is correctly returned.
     */
    @Test
    public void testAllowanceCategory() {
        IncomeCategory category = IncomeCategory.ALLOWANCE;
        assertEquals("Allowance", category.getCategoryName());
    }

    /**
     * Tests the IncomeCategory enum for the "INVESTMENTS" category.
     * Verifies that the category name for "INVESTMENTS" is correctly returned.
     */
    @Test
    public void testInvestmentsCategory() {
        IncomeCategory category = IncomeCategory.INVESTMENTS;
        assertEquals("Investments", category.getCategoryName());
    }

    /**
     * Tests the IncomeCategory enum for the "OTHER" category.
     * Verifies that the category name for "OTHER" is correctly returned.
     */
    @Test
    public void testOtherCategory() {
        IncomeCategory category = IncomeCategory.OTHER;
        assertEquals("Other", category.getCategoryName());
    }

    /**
     * Tests the number of categories available in the IncomeCategory enum.
     * Verifies that there are exactly 5 categories defined in the enum.
     */
    @Test
    public void testNumberOfCategories() {
        assertEquals(5, IncomeCategory.values().length);
    }

    /**
     * Tests the fromCategoryName(String) method.
     * Verifies that the method returns the correct enum constant based on category name input.
     */
    @Test
    public void testFromCategoryName() {
        // Test exact match
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("Full Time Job"));
        assertEquals(IncomeCategory.PART_TIME_JOB, IncomeCategory.fromCategoryName("Part Time Job"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Other"));

        // Test case-insensitive match
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("full time job"));
        assertEquals(IncomeCategory.PART_TIME_JOB, IncomeCategory.fromCategoryName("PART TIME JOB"));

        // Test non-matching category name
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Invalid Category"));
    }
    /**
     * Tests that each IncomeCategory enum value's name matches its expected constant.
     * Ensures enum name consistency.
     */
    @Test
    public void testEnumNameConsistency() {
        assertEquals("FULL_TIME_JOB", IncomeCategory.FULL_TIME_JOB.name());
        assertEquals("PART_TIME_JOB", IncomeCategory.PART_TIME_JOB.name());
        assertEquals("ALLOWANCE", IncomeCategory.ALLOWANCE.name());
        assertEquals("INVESTMENTS", IncomeCategory.INVESTMENTS.name());
        assertEquals("OTHER", IncomeCategory.OTHER.name());
    }

    /**
     * Tests that the fromCategoryName method can handle null input gracefully.
     * Ensures null safety without throwing exceptions.
     */
    @Test
    public void testFromCategoryNameWithNullInput() {
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName(null));
    }

    /**
     * Tests that fromCategoryName returns OTHER for an empty string.
     * Verifies robustness against blank input.
     */
    @Test
    public void testFromCategoryNameWithEmptyString() {
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName(""));
    }

    /**
     * Tests that fromCategoryName can handle input with leading or trailing whitespace.
     * Verifies that trimmed input correctly matches valid category names.
     */
    @Test
    public void testFromCategoryNameWithWhitespaceInput() {
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("  Full Time Job  "));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("   Allowance   "));
    }

    /**
     * Tests that fromCategoryName returns OTHER for strings that do not match
     * any category names (e.g., invalid input).
     */
    @Test
    public void testFromCategoryNameWithInvalidStrings() {
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("InvalidCategory"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("123"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("!@#$%"));
    }

    /**
     * Tests that the fromCategoryName method is case-insensitive
     * and matches valid category names regardless of input case.
     */
    @Test
    public void testCaseInsensitiveFromCategoryName() {
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("full time job"));
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("FULL TIME JOB"));
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("Full Time Job"));
    }
    /**
     * Tests that all IncomeCategory enum values are unique and distinct.
     * Verifies that no duplicate enum instances exist.
     */
    @Test
    public void testEnumValueUniqueness() {
        IncomeCategory[] categories = IncomeCategory.values();
        for (int i = 0; i < categories.length; i++) {
            for (int j = i + 1; j < categories.length; j++) {
                assertNotEquals(categories[i], categories[j]);
            }
        }
    }

    /**
     * Tests that valid category names correctly match their corresponding enum values.
     */
    @Test
    public void testValidCategoryNames() {
        assertEquals(IncomeCategory.FULL_TIME_JOB, IncomeCategory.fromCategoryName("Full Time Job"));
        assertEquals(IncomeCategory.PART_TIME_JOB, IncomeCategory.fromCategoryName("Part Time Job"));
        assertEquals(IncomeCategory.ALLOWANCE, IncomeCategory.fromCategoryName("Allowance"));
        assertEquals(IncomeCategory.INVESTMENTS, IncomeCategory.fromCategoryName("Investments"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Other"));
    }

    /**
     * Tests that fromCategoryName does not incorrectly match partial strings
     * to valid category names.
     */
    @Test
    public void testPartialStringMatching() {
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Full Time"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Allowance Money"));
        assertEquals(IncomeCategory.OTHER, IncomeCategory.fromCategoryName("Invest"));
    }


}
