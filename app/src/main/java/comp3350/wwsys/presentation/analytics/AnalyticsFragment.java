package comp3350.wwsys.presentation.analytics;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import comp3350.wwsys.R;
import comp3350.wwsys.business.DateGetter;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.StringConfig;
import comp3350.wwsys.business.UserService;

import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.Config;

/**
 * AnalyticsFragment: this shows one of our main dashboards.
 * It displays the user analytics and statistics.
 * Users can review their financial activities in detail here.
 */
public class AnalyticsFragment extends Fragment {
    private PieChart incomePieChart, expensePieChart;
    private User user;
    private EntryService entryService;
    private PieChart summaryPieChart;
    private BarChart summaryBarChart,incomeBarChart,expenseBarChart;

    private Button summaryWeekBtn, summaryMonthBtn, summaryYearBtn;
    private Button incomeWeekBtn, incomeMonthBtn, incomeYearBtn;
    private Button expenseWeekBtn, expenseMonthBtn, expenseYearBtn;


    /**
     * Default constructor for AnalyticsFragment.
     * Required for proper fragment instantiation.
     */
    public AnalyticsFragment() {}

    /**
     * Called to create and return the fragment's view hierarchy.
     *
     * @param inflater  The LayoutInflater used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the fragment's previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_analytics, container, false);

        assignViews(mainView);

        summaryWeekBtn.setOnClickListener(v->summaryWeekBtnClicked());
        summaryMonthBtn.setOnClickListener(v->summaryMonthBtnClicked());
        summaryYearBtn.setOnClickListener(v->summaryYearBtnClicked());

        incomeWeekBtn.setOnClickListener(v->incomeWeekBtnClicked());
        incomeMonthBtn.setOnClickListener(v->incomeMonthBtnClicked());
        incomeYearBtn.setOnClickListener(v->incomeYearBtnClicked());

        expenseWeekBtn.setOnClickListener(v->expenseWeekBtnClicked());
        expenseMonthBtn.setOnClickListener(v->expenseMonthBtnClicked());
        expenseYearBtn.setOnClickListener(v->expenseYearBtnClicked());

        return mainView;
    }

    /**
     * This method is called to show the analytics for the week for Expense.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the week.
     */
    private void expenseWeekBtnClicked(){
        expenseWeekBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        expenseWeekBtn.setTextColor(android.graphics.Color.WHITE);

        expenseMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        expenseYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        ArrayList<PieEntry> pieEntries;

        try {
            pieEntries = entryService.getPieChartDataWeek(user, StringConfig.EXPENSE_TYPE);
            PieChartAnalytics graphs = new PieChartAnalytics(expensePieChart, getContext());
            graphs.showPieChart(pieEntries, Config.EntryColors(getContext()));

            ArrayList<BarEntry> incomeEntries;
            String[] labels;
            incomeEntries = entryService.getWeeklyData(user, StringConfig.EXPENSE_TYPE);
            labels = DateGetter.getStringListOfWeek();
            BarChartAnalytics barGraph = new BarChartAnalytics(expenseBarChart, getContext());
            barGraph.showBarChart(incomeEntries, Config.EntryColors(getContext())[0], labels, StringConfig.EXPENSE_TYPE);
        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called to show the analytics for the month for Expense.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the month.
     */
    private void expenseMonthBtnClicked(){
        expenseMonthBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        expenseMonthBtn.setTextColor(android.graphics.Color.WHITE);

        expenseWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        expenseYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        try{

        ArrayList<PieEntry> pieEntries;
        pieEntries = entryService.getPieChartDataMonth(user, StringConfig.EXPENSE_TYPE);
        PieChartAnalytics graphs = new PieChartAnalytics(expensePieChart, getContext());
        graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));

        ArrayList<BarEntry> incomeEntries;
        String[] labels;
        incomeEntries = entryService.getMonthlyData(user, StringConfig.EXPENSE_TYPE);
        labels = DateGetter.getStringListOfMonths();
        BarChartAnalytics barGraph = new BarChartAnalytics(expenseBarChart, getContext());
        barGraph.showBarChart(incomeEntries,Config.EntryColors(getContext())[0],labels,StringConfig.EXPENSE_TYPE);
        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called to show the analytics for the year for Expense.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the year.
     */
    private void expenseYearBtnClicked(){
        expenseYearBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        expenseYearBtn.setTextColor(android.graphics.Color.WHITE);

        expenseMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        expenseWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        expenseWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        try {
            ArrayList<PieEntry> pieEntries;
            pieEntries = entryService.getPieChartDataYear(user, StringConfig.EXPENSE_TYPE);
            PieChartAnalytics graphs = new PieChartAnalytics(expensePieChart, getContext());
            graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));
            ArrayList<BarEntry> incomeEntries;
            String[] labels;
            incomeEntries = entryService.getYearData(user, StringConfig.EXPENSE_TYPE);
            labels = DateGetter.getStringListOfYears();
            BarChartAnalytics barGraph = new BarChartAnalytics(expenseBarChart, getContext());
            barGraph.showBarChart(incomeEntries,Config.EntryColors(getContext())[0],labels,StringConfig.EXPENSE_TYPE);
        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called to show the analytics for the week for Income.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the week.
     */
    private void incomeWeekBtnClicked(){
        incomeWeekBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        incomeWeekBtn.setTextColor(android.graphics.Color.WHITE);

        incomeMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        incomeYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        ArrayList<PieEntry> pieEntries;

        try{
            pieEntries = entryService.getPieChartDataWeek(user, StringConfig.INCOME_TYPE);
            PieChartAnalytics graphs = new PieChartAnalytics(incomePieChart, getContext());
            graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));

            ArrayList<BarEntry> incomeEntries;
            String[] labels;
            incomeEntries = entryService.getWeeklyData(user, StringConfig.INCOME_TYPE);
            labels = DateGetter.getStringListOfWeek();
            BarChartAnalytics barGraph = new BarChartAnalytics(incomeBarChart, getContext());
            barGraph.showBarChart(incomeEntries,Config.EntryColors(getContext())[0],labels,StringConfig.INCOME_TYPE);


        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called to show the analytics for the month for Income.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the month.
     */
    private void incomeMonthBtnClicked(){
        incomeMonthBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        incomeMonthBtn.setTextColor(android.graphics.Color.WHITE);

        incomeWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        incomeYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));
    try{

        ArrayList<PieEntry> pieEntries;
        pieEntries = entryService.getPieChartDataMonth(user, StringConfig.INCOME_TYPE);
        PieChartAnalytics graphs = new PieChartAnalytics(incomePieChart, getContext());
        graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));

        ArrayList<BarEntry> incomeEntries;
        String[] labels;
        incomeEntries = entryService.getMonthlyData(user, StringConfig.INCOME_TYPE);
        labels = DateGetter.getStringListOfMonths();
        BarChartAnalytics barGraph = new BarChartAnalytics(incomeBarChart, getContext());
        barGraph.showBarChart(incomeEntries,Config.EntryColors(getContext())[0],labels,StringConfig.INCOME_TYPE);
        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
    }


    }

    /**
     * This method is called to show the analytics for the year for Income.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the year.
     */
    private void incomeYearBtnClicked(){
        incomeYearBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        incomeYearBtn.setTextColor(android.graphics.Color.WHITE);

        incomeMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        incomeWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        incomeWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        try {
            ArrayList<PieEntry> pieEntries;
            pieEntries = entryService.getPieChartDataYear(user, StringConfig.INCOME_TYPE);
            PieChartAnalytics graphs = new PieChartAnalytics(incomePieChart, getContext());
            graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));
            ArrayList<BarEntry> incomeEntries;
            String[] labels;
            incomeEntries = entryService.getYearData(user, StringConfig.INCOME_TYPE);
            labels = DateGetter.getStringListOfYears();
            BarChartAnalytics barGraph = new BarChartAnalytics(incomeBarChart, getContext());
            barGraph.showBarChart(incomeEntries,Config.EntryColors(getContext())[0],labels,StringConfig.INCOME_TYPE);
        }catch (Exception e){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called to show the analytics for the week for Summary.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the week.
     */

    private void summaryWeekBtnClicked(){
        summaryWeekBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        summaryWeekBtn.setTextColor(android.graphics.Color.WHITE);

        summaryMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        summaryYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        ArrayList<PieEntry> pieEntries;

        pieEntries = entryService.getPieChartDataSummary(user, StringConfig.DURATION_WEEK);
        PieChartAnalytics graphs = new PieChartAnalytics(summaryPieChart, getContext());
        graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));

        ArrayList<BarEntry> incomeEntries;
        ArrayList<BarEntry> expenseEntries;
        String[] labels;

        try{
        incomeEntries = entryService.getWeeklyData(user, StringConfig.INCOME_TYPE);
        expenseEntries = entryService.getWeeklyData(user, StringConfig.EXPENSE_TYPE);
        labels = DateGetter.getStringListOfWeek();
        BarChartAnalytics barGraph = new BarChartAnalytics(summaryBarChart, getContext());
        barGraph.showGroupedBarChart(incomeEntries,expenseEntries,Config.EntryColors(getContext()),labels);
        }catch (Exception e ){
            Toast.makeText(getContext(),"Please add some entries to see the analytics", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * This method is called to show the analytics for the month for Summary.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the month.
     */

    private void summaryMonthBtnClicked(){

        summaryMonthBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        summaryMonthBtn.setTextColor(android.graphics.Color.WHITE);

        summaryWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        summaryYearBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryYearBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        ArrayList<PieEntry> pieEntries;
        pieEntries = entryService.getPieChartDataSummary(user, StringConfig.DURATION_MONTH);
        PieChartAnalytics graphs = new PieChartAnalytics(summaryPieChart, getContext());
        graphs.showPieChart(pieEntries,Config.EntryColors(getContext()));

        ArrayList<BarEntry> incomeEntries;
        ArrayList<BarEntry> expenseEntries;
        String[] labels;
        incomeEntries = entryService.getMonthlyData(user, StringConfig.INCOME_TYPE);
        expenseEntries = entryService.getMonthlyData(user, StringConfig.EXPENSE_TYPE);
        labels = DateGetter.getStringListOfMonths();
        BarChartAnalytics barGraph = new BarChartAnalytics(summaryBarChart, getContext());
        barGraph.showGroupedBarChart(incomeEntries,expenseEntries,Config.EntryColors(getContext()),labels);


    }

    /**
     * This method is called to show the analytics for the year for Summary.
     * It sets the background color and text color for the buttons
     * and updates the pie chart and bar chart with the data for the year.
     */
    private void summaryYearBtnClicked(){

        summaryYearBtn.setBackgroundColor(getResources().getColor(R.color.ios_blue, null));
        summaryYearBtn.setTextColor(android.graphics.Color.WHITE);

        summaryMonthBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryMonthBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        summaryWeekBtn.setBackgroundColor(getResources().getColor(R.color.white, null));
        summaryWeekBtn.setTextColor(getResources().getColor(R.color.ios_blue, null));

        ArrayList<PieEntry> pieEntries;
        pieEntries = entryService.getPieChartDataSummary(user, StringConfig.DURATION_YEAR);
        PieChartAnalytics pieChart = new PieChartAnalytics(summaryPieChart, getContext());
        pieChart.showPieChart(pieEntries,Config.EntryColors(getContext()));

        ArrayList<BarEntry> incomeEntries;
        ArrayList<BarEntry> expenseEntries;
        String[] labels;

        incomeEntries = entryService.getYearData(user, StringConfig.INCOME_TYPE);
        expenseEntries = entryService.getYearData(user, StringConfig.EXPENSE_TYPE);
        labels = DateGetter.getStringListOfYears();

        BarChartAnalytics barGraph = new BarChartAnalytics(summaryBarChart, getContext());
        barGraph.showGroupedBarChart(incomeEntries,expenseEntries,Config.EntryColors(getContext()),labels);



    }



    /**
     * Assigns the views for the AnalyticsFragment.
     *
     * @param mainView -
     */
    private void assignViews(View mainView) {

        entryService = new EntryService();
        UserService userService = Config.UserService();
        try{
        user = userService.getCurrentUser();
        }catch (Exception e){
            Log.d("Prititaliya", "assignViews: "+e.getMessage());
        }

        Config.setGreetingMessage(mainView, userService);

        summaryPieChart = mainView.findViewById(R.id.summaryPieChart);
        summaryBarChart = mainView.findViewById(R.id.summaryBarChart);
        summaryWeekBtn= mainView.findViewById(R.id.summary_btn_week);
        summaryMonthBtn= mainView.findViewById(R.id.summary_btn_months);
        summaryYearBtn= mainView.findViewById(R.id.summary_btn_year);
        summaryMonthBtnClicked();

        incomePieChart = mainView.findViewById(R.id.incomePieChart);
        incomeBarChart = mainView.findViewById(R.id.incomeBarChart);
        incomeWeekBtn= mainView.findViewById(R.id.income_btn_week);
        incomeMonthBtn= mainView.findViewById(R.id.income_btn_months);
        incomeYearBtn= mainView.findViewById(R.id.income_btn_year);
        incomeMonthBtnClicked();

        expenseBarChart = mainView.findViewById(R.id.expenseBarChart);
        expensePieChart = mainView.findViewById(R.id.expensePieChart);
        expenseWeekBtn= mainView.findViewById(R.id.expense_btn_week);
        expenseMonthBtn= mainView.findViewById(R.id.expense_btn_months);
        expenseYearBtn= mainView.findViewById(R.id.expense_btn_year);
        expenseMonthBtnClicked();


    }



}