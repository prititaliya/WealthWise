package comp3350.wwsys.presentation.profile;

import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;




import comp3350.wwsys.R;
import comp3350.wwsys.business.UserEntryInputValidationException;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.business.UserValidator;
import comp3350.wwsys.business.ValidateUserEntryInput;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.presentation.MainActivity;

/**
 * EditProfileFragment: this fragment allows the user to edit their profile.
 */
public class EditProfileFragment extends Fragment {
    // Variables
    private final UserService userService;
    private User user;
    private EditText firstName, lastName, email, password, newPassword, confirmPassword;
    private View mainView;


    public EditProfileFragment() {
        this.userService = Config.UserService();
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        assignViews();
        setHeaderTitle();
        populateData();
        return mainView;
    }


    /**
     * Asks the user to confirm the any changes of the entry
     */
    private void alertBox(Runnable action){
        new AlertDialog.Builder(requireContext())
                .setMessage("Are you sure you want to delete this account?")
                .setTitle("Delete User")
                .setPositiveButton("Yes", (dialog, which) -> action.run())
                .setNegativeButton("Cancel", null)
                .show();
    }
    /**
     * Assign views
     */
    private void assignViews() {

        ImageView backButton = mainView.findViewById(R.id.backButton);

        firstName = mainView.findViewById(R.id.editFirstName);
        lastName = mainView.findViewById(R.id.editLastName);
        email = mainView.findViewById(R.id.editEmail);
        password = mainView.findViewById(R.id.editCurrentPassword);
        newPassword = mainView.findViewById(R.id.editNewPassword);
        confirmPassword = mainView.findViewById(R.id.editConfirmPassword);

        LinearLayout backButtonLayout = mainView.findViewById(R.id.backButtonLayout);

        Button saveButton = mainView.findViewById(R.id.saveProfileButton);
        Button deleteButton = mainView.findViewById(R.id.deleteAccountButton);

        deleteButton.setOnClickListener(view -> alertBox(this::deleteAccount));
        saveButton.setOnClickListener(view -> saveProfile());
        backButtonLayout.setOnClickListener(v -> navigateBackToProfile());
        backButton.setOnClickListener(v -> navigateBackToProfile());
    }

    /**
     * Populate data
     */
    private void populateData() {
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
    }

    /**
     * Save profile
     */
    private void saveProfile() {
        User updatedUser;
        try {
            if(password.getText().toString().isEmpty() && !newPassword.getText().toString().isEmpty() ){
                throw new IllegalArgumentException("Please enter your current password");
            }
                ValidateUserEntryInput.validatePasswordChange(password.getText().toString());
            try {

                UserValidator.checkCurrentPassword(user,password.getText().toString());
                UserValidator.checkPasswordMatch(newPassword.getText().toString(), confirmPassword.getText().toString());

                updatedUser  = new User(user.getUserID(), firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), confirmPassword.getText().toString());
                userService.updateUser(updatedUser,confirmPassword.getText().toString());

                getParentFragmentManager().popBackStack();
                Toast.makeText(getContext(), "Password updated Successfully", Toast.LENGTH_SHORT).show();

            } catch (UserValidationException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (IllegalArgumentException e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (UserEntryInputValidationException e) {
            try {
                updatedUser  = new User(user.getUserID(), firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), user.getPassword());
                userService.updateUserInfo(updatedUser);
                getParentFragmentManager().popBackStack();
                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            } catch (UserValidationException ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * delete account
     */

    private void deleteAccount() {
        try {
            userService.deleteAccount(user);
            Config.saveLogin(mainView,"");

            goLoginActivity();
            Toast.makeText(getContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void goLoginActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the header title
     */
    private void setHeaderTitle() {
        TextView titleView = mainView.findViewById(R.id.userNameTitleView);
        try{
            titleView.setText(getString(R.string.edit_profile_text));
        }catch (Exception e){
           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigate back to profile fragment
     */
    private void navigateBackToProfile() {
        getParentFragmentManager().popBackStack();
    }

} // Edit Profile