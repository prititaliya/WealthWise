package comp3350.wwsys.presentation.analytics;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class BarChartAnalytics {


    private final Context context;
    private final BarChart barChart;

    public BarChartAnalytics(BarChart barChart, Context context){

        this.context = context;
        this.barChart = barChart;
    }


    public void showBarChart(ArrayList<BarEntry> entries, int color, String[] label,String title) {
        BarDataSet dataSet = new BarDataSet(entries, title);
        dataSet.setColor(color);
        dataSet.setValueTextColor(ContextCompat.getColor(context, android.R.color.black));

        BarData barData = new BarData(dataSet);
        dataSet.setDrawValues(false);

        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(label.length);
        xAxis.setLabelRotationAngle(-60);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setFitBars(true);
        barChart.getLegend().setEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }
    public void showGroupedBarChart(ArrayList<BarEntry> incomeEntries, ArrayList<BarEntry> expenseEntries, int[] colors, String[] labels) {
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Income");
        incomeDataSet.setColor(colors[0]);
        incomeDataSet.setValueTextColor(ContextCompat.getColor(context, android.R.color.black));

        BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expense");
        expenseDataSet.setColor(colors[1]);
        expenseDataSet.setValueTextColor(ContextCompat.getColor(context, android.R.color.black));

        BarData barData = new BarData(incomeDataSet, expenseDataSet);
        incomeDataSet.setDrawValues(false);
        expenseDataSet.setDrawValues(false);

        float groupSpace = 0.4f;
        float barSpace = 0.1f;
        float barWidth = 0.20f;

        barData.setBarWidth(barWidth);
        barChart.setData(barData);

        barChart.groupBars(-0.5f, groupSpace, barSpace);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.length);
        xAxis.setLabelRotationAngle(-60);
        xAxis.setDrawGridLines(false);

        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(labels.length - 0.5f);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f);



        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setFitBars(true);
        barChart.getLegend().setEnabled(true);

        barChart.animateY(1000);
        barChart.invalidate();
    }
}
