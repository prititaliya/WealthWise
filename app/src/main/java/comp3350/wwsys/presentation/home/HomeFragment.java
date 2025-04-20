package comp3350.wwsys.presentation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.presentation.Config;

/**
 * HomeFragment: this shows one of our main dashboards.
 * It displays the home dashboard, which users see after they log in.
 */
public class HomeFragment extends Fragment {

    private final UserService userService;
    private final EntryService entryService;

    View mainView;

    /**
     * Constructs a HomeFragment with the provided UserService.
     */
    public HomeFragment() {
        userService = Config.UserService();
        entryService = new EntryService();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        TextView dateTextView = mainView.findViewById(R.id.todaysDate);
        dateTextView.setText(getFormattedTodayDate());

        Config.setGreetingMessage(mainView,userService);
        setMonthlyTotal();
        return mainView;
    }


    /**
     * Gets today's date formatted as "MMM d, yyyy".
     *
     * @return A formatted date string for today's date.
     */
    private String getFormattedTodayDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return LocalDate.now().format(formatter);
    }

    /**
     * Retrieves and displays the user's monthly income. This method formats the income
     * as currency and updates the corresponding UI element to display it.
     */
    private void setMonthlyTotal() {
            TextView monthlyIncomeValue = mainView.findViewById(R.id.monthly_income_summary);
            TextView monthlyExpenseValue = mainView.findViewById(R.id.monthly_expense_summary);
            TextView monthlySavingsValue = mainView.findViewById(R.id.monthly_savings_summary);
            TextView netSavingsValue = mainView.findViewById(R.id.net_savings);
            monthlyIncomeValue.setText(Config.setMonthlyIncome(entryService));
            monthlyExpenseValue.setText(Config.setMonthlyExpense(entryService));
            monthlySavingsValue.setText(Config.setMonthlySavings(entryService));
            netSavingsValue.setText(Config.setMonthlySavings(entryService));
    }



} // HomeFragment