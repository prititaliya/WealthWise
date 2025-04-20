package comp3350.wwsys.presentation.tracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import comp3350.wwsys.business.EntryValidationException;

import java.util.ArrayList;


import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;

import comp3350.wwsys.business.StringConfig;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.User;


/**
 *  BaseHistoryFragment class for displaying history
 *  child classes : IncomeHistoryFragment, ExpenseHistoryFragment
 */
public class EntryHistoryFragment extends Fragment {

    private  View mainView;
    protected RecyclerView historyRecyclerView;
    protected HistoryAdapter historyAdapter;

    private final String historyTitle;
    private final UserService userService;
    private final String entryType;



    public EntryHistoryFragment(String historyTitle, UserService userService, String type) {
        this.historyTitle = historyTitle;
        entryType = type;
        this.userService = userService;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_base_history, container, false);
        assignViews();

        return mainView;
    }


    @Override
    public void onResume() {
        super.onResume();
        makeList();
    }


    private void makeList() {
        try {
            EntryService entryService = new EntryService();
            User user = userService.getCurrentUser();
            ArrayList<Entry> entriesList = new ArrayList<>();
            if (entryType.equals(StringConfig.INCOME_TYPE))  {
                entriesList = entryService.getIncomeEntries(user);
            } else if (entryType.equals(StringConfig.EXPENSE_TYPE)) {
                entriesList = entryService.getExpenseEntries(user);
            }
            HistoryFragmentHelper.setHeaderTitle(historyTitle,mainView.findViewById(R.id.userNameTitleView));
            HistoryFragmentHelper.setAdapterData(historyAdapter,entriesList);
        } catch (UserValidationException | EntryValidationException e) {
            Log.e("EntryHistoryFragment", e.toString());
        }
    }


    /**
     * Assigns the views
     */
    void assignViews(){
        View backButtonLayout = mainView.findViewById(R.id.backHistoryLayout);
        historyRecyclerView = mainView.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(new ArrayList<>(),requireContext());
        historyRecyclerView.setAdapter(historyAdapter);

        makeList();

        backButtonLayout.setOnClickListener(v->getParentFragmentManager().popBackStack());
    }

}

