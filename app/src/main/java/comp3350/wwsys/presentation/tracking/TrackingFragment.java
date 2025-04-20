package comp3350.wwsys.presentation.tracking;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.EntryValidationException;
import comp3350.wwsys.business.StringConfig;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.presentation.Config;

/**
 * A Fragment that allows the user to track their income and expenses.
 * Displays the user's monthly income, recent income entries, and provides
 * options to add new income or expense entries.
 */
public class TrackingFragment extends Fragment {

    private View mainView;
    private final UserService userService;
    private TextView monthlyIncomeValue;
    private TextView monthlyExpenseValue;
    private TextView savingsValue;
    private Fragment addIncomeFragment, addExpenseFragment;
    private final EntryService entryService;
    private RecentAcitivityAdapter recentIncomeActivityAdapter, recentExpenseActivityAdapter;

    /**
     * Constructor for the TrackingFragment.
     *
     */
    public TrackingFragment() {
        this.userService = Config.UserService();
        this.entryService = new EntryService();
    }

    /**
     * Called to create the view for the TrackingFragment.
     * This method inflates the layout, assigns the necessary views, and sets up click listeners for buttons.
     * It also loads recent entries and sets the monthly totals.
     *
     * @param inflater The LayoutInflater object used to inflate the layout.
     * @param container The container in which the fragment's UI will be placed.
     * @param savedInstanceState A Bundle containing any saved instance state.
     * @return The View for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_tracking, container, false);
        assignViews();
        loadRecentEntries();
        setMonthlyTotals();
        return mainView;
    }

    /**
     * seeMore for expense history
     */
    private void seeMoreExpenseClicked() {
        try {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new EntryHistoryFragment( "Expense History", userService, StringConfig.EXPENSE_TYPE))
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            Log.e("TrackingFragment", "Error: " + e.getMessage());
        }
    }

    /**
     * Handles the click event for the "See More" button.
     * Navigates to the Income History Fragment.
     */
    private void seeMoreIncomeClicked() {
        try {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new EntryHistoryFragment( "Income History", userService, StringConfig.INCOME_TYPE))
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("TrackingFragment", "Error: " + e.getMessage());

        }
    }

    /**
     * Sets the monthly income displayed in the fragment.
     * Fetches the current user's income and formats it to display.
     */
    private void setMonthlyTotals() {
            monthlyIncomeValue.setText(Config.setMonthlyIncome(entryService));
            monthlyExpenseValue.setText(Config.setMonthlyExpense(entryService));
            savingsValue.setText(Config.setMonthlySavings(entryService));
    }

    /**
     * Loads recent Entries
     */
    private void loadRecentEntries() {

        try{
            List<Entry> recentIncome = entryService.getMostRecentIncomeEntries(userService.getCurrentUser(), 5);
            recentIncomeActivityAdapter.setData(recentIncome);
        }catch (UserValidationException | EntryValidationException e){
            Log.e("TrackingFragment", "Error: " + e.getMessage());
        }

        try {
            List<Entry> recentExpense = entryService.getMostRecentExpenseEntries(userService.getCurrentUser(), 5);
            recentExpenseActivityAdapter.setData(recentExpense);
        } catch (UserValidationException | EntryValidationException e) {
            Log.e("TrackingFragment", "Error: " + e.getMessage());
        }

    }

    /**
     * Handles the click event for the "Add Income" button.
     * Navigates to the Add Income Fragment.
     */
    private void incomeButtonClicked() {
        try {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addIncomeFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("TrackingFragment", "Error: " + e.getMessage());

        }
    }

    /**
     * Handles the click event for the "Add Expense" button.
     * Navigates to the Add Expense Fragment.
     */
    private void expenseButtonClicked() {
        try {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addExpenseFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("TrackingFragment", "Error: " + e.getMessage());
        }
    }

    /**
     * Assigns all the views from the layout XML to their respective variables.
     */
    private void assignViews() {

        Config.setGreetingMessage(mainView,userService);

        monthlyIncomeValue = mainView.findViewById(R.id.income_value);
        monthlyExpenseValue = mainView.findViewById(R.id.expense_value);
        savingsValue = mainView.findViewById(R.id.savings_value);

        Button addIncomeButton = mainView.findViewById(R.id.income_add_btn);
        Button addExpenseButton = mainView.findViewById(R.id.expense_add_btn);

        TextView seeMoreIncomeView = mainView.findViewById(R.id.income_see_more);
        TextView seeMoreExpenseView = mainView.findViewById(R.id.expense_see_more);

        RecyclerView recentIncomeRecyclerView = mainView.findViewById(R.id.income_recent_activity);
        RecyclerView recentExpenseRecyclerView = mainView.findViewById(R.id.expense_recent_activity);

        addIncomeFragment = new AddEntryFragment(userService,"Add Income", StringConfig.INCOME_TYPE);
        addExpenseFragment = new AddEntryFragment(userService,"Add Expense",StringConfig.EXPENSE_TYPE);
        recentIncomeActivityAdapter = new RecentAcitivityAdapter(new ArrayList<>());
        recentExpenseActivityAdapter = new RecentAcitivityAdapter(new ArrayList<>());


        recentIncomeRecyclerView.setAdapter(recentIncomeActivityAdapter);
        recentExpenseRecyclerView.setAdapter(recentExpenseActivityAdapter);
        recentIncomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addIncomeButton.setOnClickListener(v -> incomeButtonClicked());
        seeMoreIncomeView.setOnClickListener(v -> seeMoreIncomeClicked());
        seeMoreExpenseView.setOnClickListener(v-> seeMoreExpenseClicked());
        addExpenseButton.setOnClickListener(v -> expenseButtonClicked());
    }

}// Tracking Fragment
