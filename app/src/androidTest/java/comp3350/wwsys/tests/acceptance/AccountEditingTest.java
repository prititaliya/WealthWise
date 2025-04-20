package comp3350.wwsys.tests.acceptance;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
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
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountEditingTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    private String testEmail;
    private String testPassword;
    private String testFirstName;
    private String testLastName;

    // Test account credentials (after update)
    private String newEmail;
    private String newFirstName;
    private String newLastName;
    private String newPassword;

    private UserService userService;

    @Before
    public void setUp() {
        // Timestamp for unique test user account creation
        long timestamp = System.currentTimeMillis();

        // Test user credentials
        testEmail = "test" + timestamp + "@email.com";
        testFirstName = "AccountEdit";
        testLastName = "Test";
        testPassword = "Test123!";

        userService = Config.UserService();

        try {
            userService.addUser(testFirstName, testLastName, testEmail, testPassword, testPassword);
            System.out.println("Successfully created test user");
        } catch (UserValidationException e) {
            System.out.println("Could not create test user: " + e.getMessage());
        }

        // Initialize test credentials
        newEmail = "updated_email@email.com";
        newFirstName = "NewFirstName";
        newLastName = "NewLastName";
        newPassword = "NewPass123!";
    }

    @Test
    public void editAccountTest() {
        // 0. Clear any saved login credentials
        try {
            activityRule.getScenario().onActivity(activity -> activity.getSharedPreferences("userCredentials", 0)
                    .edit()
                    .clear()
                    .apply());
        } catch (Exception e) {
            System.out.println("Could not clear preferences: " + e);
        }

        // 1. Login to the app
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.email)).perform(typeText(testEmail));
        onView(withId(R.id.password)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        // Let page load...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        // 2. Verify that we're on FinanceActivity
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));

        // 3. Navigate to the profile fragment
        onView(allOf(
                withId(R.id.profile_icon),
                isClickable(),
                isDisplayed()
        )).perform(click());

        // 4. Verify original test user information (before update)
        // Check first and last name
        onView(withId(R.id.user_full_name)).check(matches(withText(Matchers.containsString(testFirstName))));
        onView(withId(R.id.user_full_name)).check(matches(withText(Matchers.containsString(testLastName))));
        // Check email
        onView(withId(R.id.user_email)).check(matches(withText(Matchers.containsString(testEmail))));

        // 5. Navigate to "Edit Profile"
        onView(withText("Edit Profile")).perform(click());

        // 6. Update account credentials
        // Update email
        onView(withId(R.id.editEmail)).perform(clearText(), typeText(newEmail));
        // Update first and last name
        onView(withId(R.id.editFirstName)).perform(clearText(), typeText(newFirstName));
        onView(withId(R.id.editLastName)).perform(clearText(), typeText(newLastName));
        // Update password
        closeSoftKeyboard();
        onView(withId(R.id.editCurrentPassword)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.editNewPassword)).perform(typeText(newPassword));
        closeSoftKeyboard();
        onView(withId(R.id.editConfirmPassword)).perform(scrollTo());
        onView(withId(R.id.editConfirmPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.editConfirmPassword)).perform(typeText(newPassword));
        closeSoftKeyboard();


        onView(withId(R.id.saveProfileButton)).perform(scrollTo());

        onView(withId(R.id.saveProfileButton)).check(matches(isDisplayed()));
        // Confirm changes
        onView(withId(R.id.saveProfileButton)).perform(click());

        // 7. Confirm we navigated back to the profile fragment
        onView(withText("Edit Profile")).check(matches(isDisplayed()));

        // 8. Confirm that the new first and last name are being displayed
        // Check first and last name
        onView(withId(R.id.user_full_name)).check(matches(withText(Matchers.containsString(newFirstName))));
        onView(withId(R.id.user_full_name)).check(matches(withText(Matchers.containsString(newLastName))));
        // Check email
        onView(withId(R.id.user_email)).check(matches(withText(Matchers.containsString(newEmail))));
    }

    @After
    public void cleanUp() {
        try {
            User testUser = userService.getUserByEmail(newEmail);
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
