package comp3350.wwsys.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import comp3350.wwsys.R;
import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.presentation.Config;
import comp3350.wwsys.presentation.FinanceActivity;

public class LoginActivity extends AppCompatActivity {

    private UserService userService;
    private View loginView;

    private EditText userEmail;
    private EditText userPassword;



    /**
     * Initializes the activity, sets up the layout, and assigns view elements.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);
        loginView = findViewById(android.R.id.content);

        userService = Config.UserService();
        assignViews();
    }

    /**
     * Attempts to authenticate the user based on the provided email and password.
     * Displays a success message and navigates to the main activity if login is successful.
     * Shows an error message if authentication fails.
     */
    private void performLogin(){

        String emailString = userEmail.getText().toString().trim();
        String passwordString = userPassword.getText().toString().trim();

        try {
            userService.login(emailString, passwordString);
            Config.saveLogin(loginView, userService.getCurrentUser().getEmail());


            Toast.makeText(this, Config.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
            goMainActivity();

        } catch (UserValidationException InvalidEmailOrPassword){
            userEmail.setError(Config.LOGIN_FAILURE);
            Log.e("LoginActivity", Config.LOGIN_FAILURE);
        }

    }

    /**
     * Navigates the user to the main FinanceActivity upon successful login.
     * Closes the current activity to prevent the user from returning to the login screen.
     */
    private void goMainActivity() {

        Intent intent = new Intent(this, FinanceActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Assigns the UI components (EditText fields, buttons, and text views) to variables
     * and sets up click listeners for login and back navigation.
     */
    private void assignViews() {

         userEmail = findViewById(R.id.email);
         userPassword = findViewById(R.id.password);

        TextView backButton = findViewById(R.id.goBack);
        Button loginButton = findViewById(R.id.loginButton);

         backButton.setOnClickListener(view -> finish());
         loginButton.setOnClickListener(view -> performLogin());
    }
}