package comp3350.wwsys.tests.acceptance;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.presentation.MainActivity;

@RunWith(AndroidJUnit4.class)

public class ExpenseAnalyticsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    private String testEmail;
    private String testPassword;
    private UserService userService;

    @Before
    public void setUp() {
        // Timestamp for unique test user account creation
        long timestamp = System.currentTimeMillis();

        // Test user credentials
        testEmail = "test" + timestamp + "@email.com";
        String testFirstName = "ExpenseAnalytics";
        String testLastName = "Test";
        testPassword = "Test123!";

        userService = Config.UserService(); // Create a new user service
        EntryService entryService = Config.EntryService(); // Create a new entry service

        try { // Try to create a new user
            userService.addUser(testFirstName, testLastName, testEmail, testPassword, testPassword);
            System.out.println("Successfully created test user");
        } catch (UserValidationException e) {
            System.out.println("Could not create test user: " + e.getMessage());
        }
        Random random = new Random();
        try{
            User user = userService.getUserByEmail(testEmail);
            String[] categoriesExpense = { "Living Expenses", "Shopping", "Transportation", "Other","Subscriptions","Food","Education","Health"};
            String[] descriptionsExpense = {
                    "Monthly rent payment",
                    "Electricity bill",
                    "Weekly grocery shopping",
                    "Bus pass renewal",
                    "Miscellaneous expenses"
            };
            List<LocalDateTime> randomExpenseDate = new ArrayList<>(); // List to store random dates
            randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(7))); // add random for last week
            randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(7)));

            for (int i=0; i<8; i++){ // last 2 years
                randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(730)));
            }

            for(int i =0;i<10;i++){ // Add entries for each random date
                float testAmountExpense = 10f + random.nextFloat() * 100f; // Random amount between 10 and 110
                String testDescriptionExpense = descriptionsExpense[random.nextInt(descriptionsExpense.length)] + ". Timestamp: " + LocalDateTime.now();
                String testCategoryExpense = categoriesExpense[random.nextInt(categoriesExpense.length)];

                entryService.addEntry(  // Add entry
                        user,
                        testAmountExpense,
                        testDescriptionExpense,
                        testCategoryExpense,
                        "Expense",
                        randomExpenseDate.get(i)
                );

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    public  void testExpenseAnalytics(){
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

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }

        onView(withId(R.id.bottom_navigation)).check(ViewAssertions.matches(isDisplayed()));

        onView(withId(R.id.analytics)).perform(click());

        // scroll down to buttons
        onView(withId(R.id.expenseToggleBtn)).perform(scrollTo());
        onView(withId(R.id.expenseToggleBtn)).check(ViewAssertions.matches(isDisplayed()));

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }
        //  click the week button
        onView(withId(R.id.expense_btn_week)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.expense_btn_week)).perform(click());

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }
        // click the year button
        onView(withId(R.id.expense_btn_year)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.expense_btn_year)).perform(click());

    }

    @After
    public void cleanUp(){ // Clean up after test
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
