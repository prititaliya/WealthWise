package comp3350.wwsys.presentation.analytics;

import android.content.Context;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import comp3350.wwsys.R;

public class PieChartAnalytics {


    private final Context context;
    private final PieChart pieChart;

    public PieChartAnalytics(PieChart pieChart, Context context) {
        this.context = context;
        this.pieChart = pieChart;
    }

    void showPieChart(ArrayList<PieEntry> entries,int[] colors) {


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(colors);
        dataSet.setValueTextColor(context.getResources().getColor(android.R.color.black));




        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setDrawEntryLabels(false);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry entry = (PieEntry) e;
                Toast.makeText(context, entry.getLabel() + ": " + entry.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                pieChart.highlightValue(null);
            }
        });




        Legend legend = pieChart.getLegend();
        legend.setTextColor(context.getResources().getColor(R.color.black, null));
        legend.setTextSize(12f);
        legend.setFormSize(12f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormToTextSpace(5f);
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(5f);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }


}
