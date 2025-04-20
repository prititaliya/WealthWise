package comp3350.wwsys.presentation.tracking;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import comp3350.wwsys.objects.Entry;

class HistoryFragmentHelper {


    public static List<Entry> entries;

    public static void setHeaderTitle(String title, TextView historyTitle){
        historyTitle.setText(title);
        historyTitle.setTypeface(null, Typeface.BOLD);
    }

    /**
     * this would set the adapter data
     * @param historyAdapter history adapter to set data
     * @param entries list of entries to set
     */
    public static void setAdapterData(HistoryAdapter historyAdapter, List<Entry> entries) {
        try {
            HistoryFragmentHelper.entries = entries;
            historyAdapter.setData(entries);
        } catch (Exception e) {
            Log.e("aa", "setAdapterData: Fuckled" );
        }
    }
}