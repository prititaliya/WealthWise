package comp3350.wwsys.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import comp3350.wwsys.R;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.presentation.MainActivity;

/*
 * ProfileFragment: this fragment displays the user's unique profile.
 */
public class ProfileFragment extends Fragment {

    private TextView profile_name;
    private TextView user_email;
    private View mainView;

    private final UserService userService;

    /**
     * Constructor for ProfileFragment
     */
    public ProfileFragment() {
        this.userService = Config.UserService();
    }

    // Methods:
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        assignViews();
        displayUserDetails();
        return mainView;
    }

    /**
     * Logout button clicked
     * This method is called when the logout button is clicked
     */
    private void logoutButtonClicked() {

        Config.saveLogin(mainView,"");

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    /**
     * Assign views
     * This method assigns views to the fragment
     */
    private void assignViews() {

        profile_name = mainView.findViewById(R.id.user_full_name);
        Button logoutButton = mainView.findViewById(R.id.btn_logout);
        user_email = mainView.findViewById(R.id.user_email);
        TextView editProfileTextView = mainView.findViewById(R.id.edit_profile);

        logoutButton.setOnClickListener(view -> logoutButtonClicked());
        editProfileTextView.setOnClickListener(view -> editProfileClicked());
    }

    /**
     * Display user details
     */
    private void displayUserDetails() {
        try {
            User user = userService.getCurrentUser();
            profile_name.setText(getString(R.string.profile_name, user.getFirstName(), user.getLastName()));
            user_email.setText(getString(R.string.profile_email, user.getEmail()));
            Config.setGreetingMessage(mainView,userService);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Cannot find user" + e, Toast.LENGTH_SHORT).show();
            Log.e("Profile Fragment", "Error with User object", e);
        }
    }

    /**
     * Edit Profile clicked
     * This method is called when the edit profile text is clicked
     */
    private void editProfileClicked() {
        EditProfileFragment editProfileFragment = new EditProfileFragment();

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }

} // Profile Fragment
