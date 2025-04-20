package comp3350.wwsys.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import comp3350.wwsys.R;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.presentation.analytics.AnalyticsFragment;
import comp3350.wwsys.presentation.home.HomeFragment;
import comp3350.wwsys.presentation.profile.ProfileFragment;
import comp3350.wwsys.presentation.tracking.TrackingFragment;

/*
 * MainActivity is the main activity of the Wealth Wise application
 * it will be shown whenever the user opens our app.
 */
public class FinanceActivity extends AppCompatActivity {

    private UserService userService;

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    private final int DEFAULT_FRAGMENT_KEY = R.id.home;
    private final int ANALYTICS_FRAGMENT_KEY = R.id.analytics;
    private final int TRACKING_FRAGMENT_KEY = R.id.tracking;
    private final int PROFILE_FRAGMENT_KEY = R.id.profile_icon;
    private final int NAVIGATION_BAR_KEY = R.id.bottom_navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = Config.UserService();
        loginUser();
        setupActivity();

    }

    private void loginUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("userCredentials", MODE_PRIVATE);
        try {
            User user = userService.getUserByEmail(sharedPreferences.getString("email", ""));
            userService.setCurrentUser(user);
        }
        catch (UserValidationException e){
            Log.e("FinanceActivity", e.toString());
        }
    }

    /**
     * Setups the main activity by handling user session, initializing fragments,
     * setting up button navigation, and loading the default fragment.
     */
    private void setupActivity() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finance);
        initializeFragments();
        initializeButtonsNavigation();
        navigateToFragment(fragmentMap.get(DEFAULT_FRAGMENT_KEY));
    }


    /**
     * Initializes the fragments and maps them to corresponding keys.
     * Each fragment is created and stored in a HashMap for quick access.
     */
    private void initializeFragments() {
        fragmentMap.put(DEFAULT_FRAGMENT_KEY, new HomeFragment());
        fragmentMap.put(ANALYTICS_FRAGMENT_KEY, new AnalyticsFragment());
        fragmentMap.put(TRACKING_FRAGMENT_KEY, new TrackingFragment());
        fragmentMap.put(PROFILE_FRAGMENT_KEY, new ProfileFragment());
    }

    /**
     * Initializes the bottom navigation buttons and sets their listeners.
     */
    private void initializeButtonsNavigation() {
        ImageView profileIcon = findViewById(PROFILE_FRAGMENT_KEY);
        BottomNavigationView bottomNavigationView = findViewById(NAVIGATION_BAR_KEY);

        bottomNavigationView.setOnItemSelectedListener(navListener);
        profileIcon.setOnClickListener(view -> navigateToFragment(fragmentMap.get(PROFILE_FRAGMENT_KEY)));
    }

    /**
     * Replaces the current fragment in the fragment container with the given fragment.
     *
     * @param fragment The fragment to display.
     */
    private void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Listener for handling bottom navigation item selections.
     * Navigates to the corresponding fragment when an item is selected.
     */
    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        boolean result = false;
        Fragment selectedFragment = fragmentMap.get(item.getItemId());
        try{
            navigateToFragment(selectedFragment);
            result = true;
        }catch (Exception e ){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return result;
    };


}