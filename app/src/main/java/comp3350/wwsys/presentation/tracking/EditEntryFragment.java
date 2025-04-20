package comp3350.wwsys.presentation.tracking;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import comp3350.wwsys.business.EntryValidationException;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;

import comp3350.wwsys.business.UserEntryInputValidationException;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;

import comp3350.wwsys.objects.Entry;
import comp3350.wwsys.objects.IncomeCategory;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.business.ValidateUserEntryInput;

/**
 * this fragment allows the user to edit an entry
 */
public class EditEntryFragment extends Fragment {
    private final UserService userService;
    // Variables:
    private View mainView;

    private EditText editEntryAmount, editEntryDescription;
    private AutoCompleteTextView editEntryCategory;
    private Entry entry;
    private final static int INCOME_DROPDOWN = android.R.layout.simple_dropdown_item_1line;
    protected AtomicReference<LocalDateTime> editEntryDate = new AtomicReference<>();
    private Button selectDate;

    private final EntryService entryService;

    /**
     * Constructor for EditEntryFragment
     * @param entry the entry to be edited
     */
    EditEntryFragment(Entry entry) {
        this.entry = entry;
        entryService = new EntryService();
        this.userService = Config.UserService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         mainView = inflater.inflate(R.layout.fragment_edit_entry, container, false);
         assignViews();
         populateFields();
         return mainView;
    }
    /**
     * Asks the user to confirm the any changes of the entry
     */
    private void alertBox(String message, String title, Runnable action){
        new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("Yes", (dialog, which) -> action.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Performs the actual deletion of the entry
     */
    private void performDelete() {
        try {
            entryService.removeEntry(entry);

            Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } catch (Exception e) {
            Log.e("EditEntryFragment", "Error: " + e.getMessage());
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Saves the changes made to the entry
     * called when the save button is clicked
     */
    private void saveChanges() {
        try{
            float amount = ValidateUserEntryInput.validateAmount(editEntryAmount.getText().toString());

            this.entry = new Entry(entry.getId(), userService.getCurrentUser().getUserID(), amount, editEntryDescription.getText().toString(), editEntryCategory.getText().toString(), entry.getTypeString(), entry.getCreatedDate(), editEntryDate.get());
            entryService.editEntry(entry);
            Toast.makeText(getContext(), "Entry Updated", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();

        }catch (UserEntryInputValidationException | EntryValidationException e){
            Log.e("EditEntryFragment", "Error: " + e.getMessage());
            Toast.makeText(getContext(),  e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (UserValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the fields with the entry's information
     * called when the fragment is created
     */
    private void populateFields() {
        editEntryAmount.setText(String.valueOf(entry.getAmount()));
        editEntryDescription.setText(entry.getDescription());
        setCategory();
        editEntryCategory.setText(entry.getCategory(), false);
        editEntryDate.set(entry.getEffectiveDate());
        selectDate.setText(entry.getEffectiveDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));

    }

    /**
     * Assigns the views to the variables
     *
     */
    private void assignViews(){
        editEntryAmount = mainView.findViewById(R.id.editAmount);
        editEntryCategory = mainView.findViewById(R.id.editCategory);
        editEntryDescription = mainView.findViewById(R.id.editDescription);

        LinearLayout backButtonLayout = mainView.findViewById(R.id.backButtonLayout);
        selectDate = mainView.findViewById(R.id.editDate);
        Button saveChangesButton = mainView.findViewById(R.id.saveChangedButton);
        Button deleteEntryButton = mainView.findViewById(R.id.deleteEntryButton);

        backButtonLayout.setOnClickListener(v->getParentFragmentManager().popBackStack());
        selectDate.setOnClickListener(view ->selectDate());

        saveChangesButton.setOnClickListener(view ->alertBox("Are you sure you want to save the changes?", "Save Changes", this::saveChanges));
        deleteEntryButton.setOnClickListener(view -> alertBox("Are you sure you want to delete this entry?", "Delete Entry", this::performDelete));
    }

    /**
     * Sets the category dropdown of the entry
     */
    protected void setCategory() {
        ArrayList<String> categoryNames = new ArrayList<>();

        for (IncomeCategory category : IncomeCategory.values()) {
            categoryNames.add(category.getCategoryName()); // Converts enum to string
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), INCOME_DROPDOWN, categoryNames);
        editEntryCategory.setAdapter(adapter);

    }

    /**
     * Selects the date for the entry
     */
    private void selectDate() {

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        picker.show(getParentFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(this::handleDateSelection);
    }
    private void handleDateSelection(Long selection) {
        if (selection != null) {
            LocalDateTime dateTime = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            editEntryDate.set(dateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String formattedDate = editEntryDate.get().format(formatter);
            selectDate.setText(formattedDate); // update date button to show the selected date
            selectDate.setTextColor(getResources().getColor(R.color.black, null)); // update colour to black
            selectDate.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL)); // unbolden item
        }
    }




} //EditEntryFragment

