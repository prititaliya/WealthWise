package comp3350.wwsys.presentation.tracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import comp3350.wwsys.R;
import comp3350.wwsys.objects.Entry;

/**
 * Adapter class for the history recycler view
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    // Variables:
    private List<Entry> entryList;
    private final Context context;

    // Constructor: which takes a list of entries
    public HistoryAdapter(List<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_entry_layout, parent, false);
       return new ViewHolder(view);

    }

    /**
     * Binds the data to the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Entry entry=entryList.get(position);
        // Format Amount
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String formattedAmount = currencyFormat.format(entry.getAmount());
        holder.amountTextView.setText(formattedAmount);

        // Format Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String formattedDate = entry.getEffectiveDate().format(formatter);
        holder.dateTextView.setText(formattedDate);

        // Set category
        holder.categoryTextView.setText(entry.getCategory());

        holder.editEntry.setOnClickListener(view -> editClicked(entry));
    }
    private void editClicked(Entry entry) {
        EditEntryFragment editEntryFragment = new EditEntryFragment(entry);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editEntryFragment)
                .addToBackStack(null)
                .commit();

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
    protected static class ViewHolder  extends  RecyclerView.ViewHolder{
        private final TextView  dateTextView;
        private final TextView amountTextView;
        private final TextView categoryTextView;
        private final ImageView editEntry;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            dateTextView=itemView.findViewById(R.id.historyDate);
            amountTextView=itemView.findViewById(R.id.historyAmount);
            categoryTextView=itemView.findViewById(R.id.historyCategory);
            editEntry=itemView.findViewById(R.id.editEntry);
        }
    }
}
