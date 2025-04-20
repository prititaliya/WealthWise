package comp3350.wwsys.tests.acceptance;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class AccountMgmtTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private String testEmail;
    private String testFirstName;
    private String testLastName;
    private String testPassword;

    private UserService userService;

    @Before
    public void setUp() {
        testFirstName = "Test";
        testLastName = "User";
        testEmail = "test@email.com";
        testPassword = "Test123!";

        userService = Config.UserService();
    }

    @Test
    public void testUserSignUp() {
        // Navigate to sign up
        onView(withId(R.id.signUpButton)).perform(click());

        // Fill fields
        onView(withId(R.id.firstName)).perform(typeText(testFirstName));
        onView(withId(R.id.lastName)).perform(typeText(testLastName));
        onView(withId(R.id.email)).perform(typeText(testEmail));
        onView(withId(R.id.password)).perform(typeText(testPassword));
        onView(withId(R.id.confirmPassword)).perform(typeText(testPassword));
        closeSoftKeyboard();

        onView(withId(R.id.signUpBtn)).perform(click());

        // Verify we're in FinanceActivity
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));

        // Navigate to the profile fragment
        onView(withId(R.id.profile_icon)).perform(click());

        // Verify test user information in the profile fragment
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
            System.out.println("Test user not found or already removed");
        }catch (Exception e) {
            System.out.println("Could not delete test user");
        }
    }
}
