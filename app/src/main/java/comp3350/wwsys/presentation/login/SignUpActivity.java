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


/**
 * SignUpActivity handles user registration.
 * Users can enter their details, sign up, and be redirected to the main application.
 */
public class SignUpActivity extends AppCompatActivity {

    private UserService userService;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    /**
     * Initializes the activity, sets up the layout, and assigns view elements.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = Config.UserService();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        assignViews();
    }

    /**
     * Attempts to register a new user using the provided input fields.
     * Saves the user's login details upon successful sign-up and redirects to the main activity.
     * Displays an error message if the sign-up process fails.
     */
    private void performSignUp() {
        String firstNameString = firstName.getText().toString().trim();
        String lastNameString = lastName.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String confirmPasswordString = confirmPassword.getText().toString().trim();
        try {
            userService.addUser(
                    firstNameString,
                    lastNameString,
                    emailString,
                    passwordString,
                    confirmPasswordString
            );
            userService.login(emailString, passwordString);

            View rootView = findViewById(android.R.id.content);
            Config.saveLogin(rootView, userService.getCurrentUser().getEmail());
            Intent intent = new Intent(this, FinanceActivity.class);
            startActivity(intent);
            finish();
        } catch (UserValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Signup", "Sign up Failed", e);
        }
    }

    /**
     * Assigns the UI components (EditText fields, buttons, and text views) to variables
     * and sets up click listeners for sign-up and back navigation.
     */
    private void assignViews() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        Button signUpBtn = findViewById(R.id.signUpBtn);
        TextView goBack = findViewById(R.id.goBack);

        signUpBtn.setOnClickListener(view -> performSignUp());
        goBack.setOnClickListener(view -> finish());
    }
}
