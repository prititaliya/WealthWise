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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountMgmtTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    private String testEmail;
    private String testFirstName;
    private String testLastName;
    private String testPassword;

    private UserService userService;

    @Before
    public void setUp() {
        // Unique test data to avoid conflicts between tests
        long timestamp = System.currentTimeMillis();
        testFirstName = "Test";
        testLastName = "User";
        testEmail = "test" + timestamp + "@email.com";
        testPassword = "Test123!";

        userService = Config.UserService();
    }

    @Test
    public void testUserSignUp() {
        // 0. Clear any saved login credentials
        try {
            activityRule.getScenario().onActivity(activity -> activity.getSharedPreferences("userCredentials", 0)
                        .edit()
                        .clear()
                        .apply());
        } catch (Exception e) {
            System.out.println("Could not clear preferences: " + e);
        }

        // 1. Navigate to sign up
        onView(withId(R.id.signUpButton)).perform(click());

        // 2. Fill fields
        onView(withId(R.id.firstName)).perform(typeText(testFirstName));
        closeSoftKeyboard();
        onView(withId(R.id.lastName)).perform(typeText(testLastName));
        closeSoftKeyboard();
        onView(withId(R.id.email)).perform(typeText(testEmail));
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.confirmPassword)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn)).perform(click());

        // Let page load...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        // 3. Verify we're in FinanceActivity
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));

        // 4. Navigate to the profile fragment
        onView(allOf(
                withId(R.id.profile_icon),
                isClickable(),
                isDisplayed()
        )).perform(click());

        // 5. Verify test user information in the profile fragment
        // Check first and last name
        onView(withId(R.id.user_full_name)).check(matches(withText(containsString(testFirstName))));
        onView(withId(R.id.user_full_name)).check(matches(withText(containsString(testLastName))));

        // Check email
        onView(withId(R.id.user_email)).check(matches(withText(containsString(testEmail))));
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

} // AccountMgmtTest