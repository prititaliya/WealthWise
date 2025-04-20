package comp3350.wwsys.presentation.tracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import comp3350.wwsys.R;
import comp3350.wwsys.objects.Entry;

/**
 * Adapter class for the recent activity recycler view
 */
public class RecentAcitivityAdapter extends RecyclerView.Adapter<RecentAcitivityAdapter.ViewHolder> {
    // Variables:
    private List<Entry> entryList;


    // Constructor: which takes a list of entries
    public RecentAcitivityAdapter(List<Entry> entryList) {
        this.entryList=entryList;
    }

    @NonNull
    @Override
    public RecentAcitivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity, parent, false);
        return new ViewHolder(view);

    }

    /**
     * Binds the data to the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull RecentAcitivityAdapter.ViewHolder holder, int position) {
        Entry entry=entryList.get(position);
        // Format Amount
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String formattedAmount = currencyFormat.format(entry.getAmount());
        holder.amountTextView.setText(formattedAmount);

        // Format Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String formattedDate = entry.getEffectiveDate().format(formatter);
        holder.dateTextView.setText(formattedDate);

        holder.categoryTextView.setText(entry.getCategory());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    /**
     * Sets the data for the adapter
     * @param entries the list of entries
     */
    public void setData(List<Entry> entries) {
        entryList.clear();
        this.entryList = entries;
    }

    /**
     * View holder class
     */
    public static class ViewHolder  extends  RecyclerView.ViewHolder{
        private final TextView  dateTextView;
        private final TextView amountTextView;
        private final TextView categoryTextView;
        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            dateTextView=itemView.findViewById(R.id.recentEntryDate);
            amountTextView=itemView.findViewById(R.id.recentEntryAmount);
            categoryTextView=itemView.findViewById(R.id.recentEntryCategory);
        }
    }
}
