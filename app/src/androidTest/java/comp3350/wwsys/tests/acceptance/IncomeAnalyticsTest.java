package comp3350.wwsys.tests.acceptance;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

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

public class IncomeAnalyticsTest {
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
        String testFirstName = "IncomeAnalytics";
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
            String[] categoriesIncome = {"Full Time Job","Part Time Job", "Investments", "Investments", "Other"};
            String[] descriptionsIncome = {
                    "Monthly salary received",
                    "Freelance payment for project",
                    "Stock dividend credited",
                    "Unexpected gift from friend",
                    "Sold old electronics"
            };
            List<LocalDateTime> randomIncomeDate = new ArrayList<>(); // List to store random dates
            randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(7))); // add random for last week
            randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(7)));

            for (int i=0; i<8; i++){ // last 2 years
                randomIncomeDate.add(LocalDateTime.now().minusDays(random.nextInt(730)));
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

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void IncomeAnalyticsTest1(){
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
        closeSoftKeyboard();
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

        // scroll down to the buttons we need
        onView(withId(R.id.incomeToggleBtn)).perform(scrollTo());
        onView(withId(R.id.incomeToggleBtn)).check(ViewAssertions.matches(isDisplayed()));

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }
        //  click the week button
        onView(withId(R.id.income_btn_week)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.income_btn_week)).perform(click());

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            System.out.println("Error: " + e);
        }

        // Click the year button
        onView(withId(R.id.income_btn_year)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.income_btn_year)).perform(click());
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
