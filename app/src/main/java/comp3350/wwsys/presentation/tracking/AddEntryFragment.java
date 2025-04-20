package comp3350.wwsys.presentation.tracking;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import comp3350.wwsys.R;
import comp3350.wwsys.business.EntryService;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.business.ValidateUserEntryInput;

import androidx.fragment.app.Fragment;


public class AddEntryFragment extends Fragment{
    // variables:
    private final String ENTRY_TYPE;
    private final static int ENTRY_DROPDOWN = android.R.layout.simple_dropdown_item_1line;
    private final EntryService entryService;
    private final String headerTitle;
    private User user;

    private EditText entryAmount, entryDescription;
    private AutoCompleteTextView entryCategory;
    private Button selectDate, addEntry;
    private View mainView;
    private final AtomicReference<LocalDateTime> entryDate = new AtomicReference<>();
    private LinearLayout backButtonLayout;


    // Constructor requiring a UserService instance, a header title, and an entry type
    public AddEntryFragment(UserService userService, String headerTitle, String ENTRY_TYPE) {
        this.entryService = new EntryService();
        this.headerTitle = headerTitle;
        this.ENTRY_TYPE = ENTRY_TYPE;
        try{
            user = userService.getCurrentUser();
        }catch (UserValidationException e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates the view for the AddEntryFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_base_entry, container, false);
        setHeader(mainView, headerTitle);
        assignViews();
        setCategory();
        selectDate.setOnClickListener(view -> selectDate());
        addEntry.setOnClickListener(view -> addEntry());
        backButtonLayout.setOnClickListener(view -> getParentFragmentManager().popBackStack());
        return mainView;
    }

    /**
     * Sets the header of the AddEntryFragment
     * @param mainView The view of the fragment
     * @param headerTitle The title of the header
     */
    private void setHeader(View mainView, String headerTitle) {
        try{
            TextView title = mainView.findViewById(R.id.userNameTitleView);
            title.setText(headerTitle);
            MaterialButton addEntryButton = mainView.findViewById(R.id.buttonAddEntry);
            addEntryButton.setText(headerTitle);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Adds an entry to the database doesnt matter type of entry
     */
    private void addEntry() {
        try{
            float amount= ValidateUserEntryInput.validateAmount(this.entryAmount.getText().toString());
            String description = this.entryDescription.getText().toString();
            String category = this.entryCategory.getText().toString();
            LocalDateTime effectiveDate = entryDate.get();

            entryService.addEntry(user, amount, description, category, ENTRY_TYPE, effectiveDate);
            Toast.makeText(getContext(), "Entry added successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Sets the category of the entry
     */
    private void setCategory() {
        ArrayList<String> categoryNames = EntryService.getCategoryNames(ENTRY_TYPE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), ENTRY_DROPDOWN, categoryNames);
        entryCategory.setAdapter(adapter);
    }

    /**
     * assigns the Views in the fragment
     */
    private void assignViews() {
        entryAmount = mainView.findViewById(R.id.entryAmount);
        entryDescription = mainView.findViewById(R.id.entryDescription);
        entryCategory = mainView.findViewById(R.id.entryCategory);
        selectDate = mainView.findViewById(R.id.buttonSelectDate);
        addEntry = mainView.findViewById(R.id.buttonAddEntry);
        backButtonLayout = mainView.findViewById(R.id.backButtonLayout);
    }


    /**
     * selectDate method
     * This method is called when the user clicks the select date button.
     * Implements the logic to select a date; this behavior is consistent across all entry types.
     */
    private void selectDate() {

        String date = "Select Date";
        selectDate.setText(date);
        selectDate.setTextColor(getResources().getColor(R.color.ios_medium_grey, null));
        selectDate.setTypeface(Typeface.create("sans-serif", Typeface.ITALIC));

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        picker.show(getParentFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(this::handleDateSelection);
    }

    /**
     * handleDateSelection method
     * This method is called when the user selects a date.
     * This is the same for all types of entries and is implemented here.
     * @param selection The selected date in milliseconds
     */
    private void handleDateSelection(Long selection) {
        if (selection != null) {
            LocalDateTime dateTime = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime()
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            entryDate.set(dateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String formattedDate = entryDate.get().format(formatter);
            selectDate.setText(formattedDate);
            selectDate.setTextColor(getResources().getColor(R.color.black, null));
            selectDate.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        }
    }



}
