package comp3350.wwsys.presentation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.NumberFormat;
import java.util.Locale;

import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;

/**
 * Config: A utility class responsible for setting user-related data, formatting values, and saving login information.
 */
public class Config {

    public static final String LOGIN_SUCCESS = "Login Successful";
    public static final String LOGIN_FAILURE = "Invalid email or password";




    private static UserService userService = null;
    private static EntryService entryService = null;

    private static final String PREF_NAME = "userCredentials";
    private static final String KEY_EMAIL = "email";

    private static final String defaultValue = "0.00";
    private static final String configTag = "Config";
    private static final String IncomeError = " Error monthly income";
    private static final String ExpenseError = " Error monthly expense";
    private static final String SavingsError = " Error monthly Saving";

    /**
     * Sets a greeting message for the user on the provided view.
     *
     * @param view          The view where the greeting message will be displayed.
     * @param userService   The service to fetch the current user's details.
     */
    public static void setGreetingMessage(View view, UserService userService) {
        try {
            User user = userService.getCurrentUser();
            String greetingMessage =  "Hello, " + user.getFirstName();

            TextView userNameTitleView = view.findViewById(R.id.userNameTitleView);
            userNameTitleView.setText(greetingMessage);

        } catch (UserValidationException e) {
            Toast.makeText(view.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Initializes and returns the UserService object.
     * If the service is not already initialized, it will create a new instance.
     *
     * @return A UserService instance.
     */
    public static UserService UserService() {
        if ( userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public static EntryService EntryService() {
        if ( entryService == null) {
            entryService = new EntryService();
        }
        return entryService;
    }

    /**
     * Saves the email of the user in shared preferences.
     *
     * @param view   The view context used to get shared preferences.
     * @param email  The email to be saved.
     */
    public static void saveLogin(View view, String email) {
        SharedPreferences preferences = view.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_EMAIL, email);  // No need for getText() since email is already a String
        editor.apply();
    }

    /**
     * Formats a given float value into a currency format.
     *
     * @param total The float value to be formatted.
     * @return A formatted string representing the currency value.
     */
    private static String formatFloat(float total) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
         return formatter.format(total);
    }

    /**
     * Sets the monthly income for the current user and returns it as a formatted string.
     *
     * @param entryService The service used to calculate the user's monthly income.
     * @return A string representing the formatted monthly income.
     */
    public static String setMonthlyIncome(EntryService entryService) {
        String result = defaultValue;
        try {
            User user = userService.getCurrentUser();
            float incomeResult = entryService.getMonthlyIncome(user);
            result = formatFloat(incomeResult);

        } catch (UserValidationException e) {
            Log.e(configTag, IncomeError);
        }

        return result;
    }


    /**
     * Sets the monthly expense for the current user and returns it as a formatted string.
     *
     * @param entryService The service used to calculate the user's monthly expenses.
     * @return A string representing the formatted monthly expenses.
     */
    public static String setMonthlyExpense(EntryService entryService) {
        String result = defaultValue;
        try {
            User user = userService.getCurrentUser();
            float expenseResult = entryService.getMonthlyExpense(user);
            result = formatFloat(expenseResult);

        } catch (UserValidationException e) {
            Log.e(configTag, ExpenseError);
        }

        return result;
    }

    /**
     * Sets the monthly savings for the current user and returns it as a formatted string.
     *
     * @param entryService The service used to calculate the user's monthly net savings.
     * @return A string representing the formatted monthly savings.
     */
    public static String setMonthlySavings(EntryService entryService) {
        String result = defaultValue;
        try {
            User user = userService.getCurrentUser();
            float savingsResult = entryService.getMonthlyNet(user);
            result = formatFloat(savingsResult);

        } catch (UserValidationException e) {
            Log.e(configTag, SavingsError);
        }

        return result;
    }


    public static int[] EntryColors(Context context) {

        return new int[]{
                ContextCompat.getColor(context, R.color.ios_green),
                ContextCompat.getColor(context, R.color.ios_red),
                ContextCompat.getColor(context, R.color.ios_blue)
        };
    }




}
