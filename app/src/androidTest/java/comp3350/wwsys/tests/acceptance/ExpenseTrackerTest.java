package comp3350.wwsys.tests.acceptance;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.wwsys.R;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.presentation.MainActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExpenseTrackerTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    private String testEmail;
    private String testPassword;

    // Test Expense Entry
    String testAmount;
    String testDescription;
    String testCategory;

    private UserService userService;

    @Before
    public void setUp() {
        // Timestamp for unique test user account creation
        long timestamp = System.currentTimeMillis();

        // Test user credentials
        testEmail = "test" + timestamp + "@email.com";
        String testFirstName = "ExpenseTracker";
        String testLastName = "Test";
        testPassword = "Test123!";

        userService = Config.UserService();

        try {
            userService.addUser(testFirstName, testLastName, testEmail, testPassword, testPassword);
            System.out.println("Successfully created test user");
        } catch (UserValidationException e) {
            System.out.println("Could not create test user: " + e.getMessage());
        }

        // Initialize test credentials
        testAmount = "100";
        testDescription = "Test expense entry. Timestamp: "+ timestamp;
        testCategory = "Shopping";
    }

    @Test
    public void testExpenseEntry() {
        // 0. Clear any saved login credentials
        try {
            activityRule.getScenario().onActivity(activity -> activity.getSharedPreferences("userCredentials", 0)
                    .edit()
                    .clear()
                    .apply());
        } catch (Exception e) {
            System.out.println("Could not clear preferences: " + e);
        }

        // 1. Login
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.email)).perform(typeText(testEmail));
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        // Let page load...
        try {
            Thread.sleep(2000); // 2 second pause
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        // 2. Verify we've on the FinanceActivity page
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));

        // 3. Navigate to the Tracking section
        onView(withId(R.id.tracking)).perform(click());

        // 4. Verify we're in the Tracking section
        onView(withId(R.id.income_add_btn)).check(matches(isDisplayed()));

        // 5. Click on add expense button
        onView(withId(R.id.expense_add_btn)).perform(scrollTo(), click());

        // 6. Verify we're on the add expense screen
        onView(withId(R.id.entryAmount)).check(matches(isDisplayed())); // Entry amount
        onView(withId(R.id.entryDescription)).check(matches(isDisplayed())); // Entry description field
        onView(withId(R.id.entryCategory)).check(matches(isDisplayed())); // Entry category field
        onView(withId(R.id.buttonSelectDate)).check(matches(isDisplayed())); // Entry date button

        // 7. Add expense entry
        // Fill fields
        onView(withId(R.id.entryAmount)).perform(typeText(testAmount));
        closeSoftKeyboard();
        onView(withId(R.id.entryDescription)).perform(typeText(testDescription));
        closeSoftKeyboard();
        // Select a category
        onView(withId(R.id.entryCategory)).perform(androidx.test.espresso.action.ViewActions.replaceText(testCategory));
        closeSoftKeyboard();
        // Select a date
        onView(withId(R.id.buttonSelectDate)).perform(click());
        onView(withText("OK")).perform(click());

        // 8. Complete the form
        onView(withId(R.id.buttonAddEntry)).perform(click());

        // 9. Verify we're back in the tracking section
        onView(withId(R.id.income_add_btn)).check(matches(isDisplayed()));

        // 10. Check that the new entry appears in the recent activities
        onView(withId(R.id.expense_recent_activity)).perform(scrollTo());
        onView(withId(R.id.expense_recent_activity)).check(matches(isDisplayed()));

        // 11. Navigate to full history to verify entry is there
        onView(withId(R.id.expense_see_more)).perform(click());

        // 12. Verify we're on the income history screen
        onView(withText("Expense History")).check(matches(isDisplayed()));

        // 13. Verify that the test expense entry is displayed correctly
        onView(withText(containsString(testAmount))).check(matches(isDisplayed()));
    }

    @After
    public void cleanUp() {
        try {
            User testUser = userService.getUserByEmail(testEmail);
            if (testUser != null) {
                userService.deleteAccount(testUser);
            }
        } catch (UserValidationException e) {
            System.out.println("Test user not found or already removed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Could not delete test user: " + e);
        }

        // Clear saved login credentials again after test
        activityRule.getScenario().onActivity(activity -> activity.getSharedPreferences("userCredentials", 0)
                .edit()
                .clear()
                .apply());
    }
}
