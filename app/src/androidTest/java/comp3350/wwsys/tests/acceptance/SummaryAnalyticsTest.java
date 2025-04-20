package comp3350.wwsys.tests.acceptance;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

/**
 * SummaryAnalyticsTest class to test the summary analytics feature
 */
@RunWith(AndroidJUnit4.class)
public class SummaryAnalyticsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    private String testEmail;
    private String testPassword;
    private UserService userService;

    /**
     * setUp method to create a test user account and add entries to the account
     * @throws UserValidationException thrown if user validation fails
     */
    @Before
    public void setUp() throws UserValidationException {
        // Timestamp for unique test user account creation
        long timestamp = System.currentTimeMillis();

        // Test user credentials
        testEmail = "test" + timestamp + "@email.com";
        String testFirstName = "IncomeTracker";
        String testLastName = "Test";
        testPassword = "Test123!";

        userService = Config.UserService(); // Create a new user service

        try { // Try to create a new user
            userService.addUser(testFirstName, testLastName, testEmail, testPassword, testPassword);
            System.out.println("Successfully created test user");
        } catch (UserValidationException e) {
            System.out.println("Could not create test user: " + e.getMessage());
        }

        // create entryService to add entries
        EntryService entryService = Config.EntryService();
        User user=userService.getUserByEmail(testEmail);

        try { // Add 10 random income entries 2 for last week and 8 for last 2 years
            String[] categoriesIncome = {"Full Time Job","Part Time Job", "Investments", "Investments", "Other"};
            String[] descriptionsIncome = {
                    "Monthly salary received",
                    "Freelance payment for project",
                    "Stock dividend credited",
                    "Unexpected gift from friend",
                    "Sold old electronics"
            };

            String[] categoriesExpense = { "Living Expenses", "Shopping", "Transportation", "Other","Subscriptions","Food","Education","Health"};
            String[] descriptionsExpense = {
                    "Monthly rent payment",
                    "Electricity bill",
                    "Weekly grocery shopping",
                    "Bus pass renewal",
                    "Miscellaneous expenses"
            };
            Random random = new Random(); // Random object to generate random values

            List<LocalDateTime> randomIncomeDate = new ArrayList<>(); // List to store random dates
            randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(7))); // add random for last week
            randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(7)));


            List<LocalDateTime> randomExpenseDate = new ArrayList<>(); // same as expense
            randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(7)));
            randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(7)));
            for (int i=0; i<8; i++){ // last 2 years
                randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(730)));
                randomExpenseDate.add(LocalDateTime.now().minusDays(random.nextInt(730)));

            }
           for(int i =0;i<10;i++){ // Add entries for each random date
                float testAmountIncome = 10f + random.nextFloat() * 100f; // Random amount between 10 and 110
                String testDescriptionIncome = descriptionsIncome[random.nextInt(descriptionsIncome.length)] + ". Timestamp: " + LocalDateTime.now();
                String testCategoryIncome = categoriesIncome[random.nextInt(categoriesIncome.length)];

                entryService.addEntry(  // Add entry
                          user,
                          testAmountIncome,
                          testDescriptionIncome,
                          testCategoryIncome,
                          "Income",
                          randomIncomeDate.get(i)
                );
               float testAmountExpense = 10f + random.nextFloat() * 100f; // Random amount between 10 and 110
               String testDescriptionExpense = descriptionsExpense[random.nextInt(descriptionsExpense.length)] + ". Timestamp: " + LocalDateTime.now();
               String testCategoryExpense = categoriesExpense[random.nextInt(categoriesExpense.length)];

               entryService.addEntry(
                       user,
                       testAmountExpense,
                       testDescriptionExpense,
                       testCategoryExpense,
                       "Expense",
                       randomExpenseDate.get(i)
               );
           }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void SummaryTest()  {

        // login with test user
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.email)).perform(typeText(testEmail));
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText(testPassword));
        closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());

        // Let page load...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()));

        // go to analytics
        onView(withId(R.id.analytics)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        // check if the summary bar chart is displayed
        onView(withId(R.id.summaryBarChart)).perform(scrollTo());
        onView(withId(R.id.summaryBarChart)).check(matches(isDisplayed()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }

        // check if the time line toggle button is displayed
        onView(withId(R.id.timeLineToggalBtn)).perform(scrollTo());
        onView(withId(R.id.timeLineToggalBtn)).check(matches(isDisplayed()));

        // select week
        onView(withId(R.id.timeLineToggalBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.summary_btn_week)).perform(click());

        onView(withId(R.id.summaryBarChart)).perform(scrollTo());
        onView(withId(R.id.summaryBarChart)).check(matches(isDisplayed()));

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }


        onView(withId(R.id.summaryBarChart)).perform(scrollTo());
        onView(withId(R.id.summaryBarChart)).check(matches(isDisplayed()));

        // select year
        onView(withId(R.id.summary_btn_year)).perform(click());

        onView(withId(R.id.timeLineToggalBtn)).perform(scrollTo());
        onView(withId(R.id.timeLineToggalBtn)).check(matches(isDisplayed()));
    }

    @After
    public void cleanUp(){
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
