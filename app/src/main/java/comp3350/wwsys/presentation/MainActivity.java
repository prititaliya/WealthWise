package comp3350.wwsys.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import comp3350.wwsys.R;
import comp3350.wwsys.application.Main;
import comp3350.wwsys.presentation.login.LoginActivity;
import comp3350.wwsys.presentation.login.SignUpActivity;

/*
* when user open application first time after downloading it, this activity will be shown
* user can login or sign up from here
* */
public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        copyDatabaseToDevice();
        assignViews();
        handleUserSession();
    }

    /**
     * Assigns UI components to their respective variables and sets up click listeners.
     */
    private void assignViews() {
        loginBtn = findViewById(R.id.loginButton);
        signUpBtn = findViewById(R.id.signUpButton);
        loginBtn.setOnClickListener(view -> performLogin());
        signUpBtn.setOnClickListener(view -> sendToSignUp());
    }

    /**
     * Navigates to the SignUpActivity.
     */
    private void sendToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("Parent", "MainActivity");
        startActivity(intent);
    }

    /**
     * Navigates to the LoginActivity.
     */
    private void performLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("Parent", "MainActivity");
        startActivity(intent);
    }

    /**
     * Checks if there is an active user session by reading stored user credentials.
     * If a user email is found, navigate directly to the FinanceActivity.
     */
    private void handleUserSession() {
        SharedPreferences prefs = getSharedPreferences("userCredentials", MODE_PRIVATE);
        String userEmail = prefs.getString("email", "");

        if (!userEmail.isEmpty()) {
            navigateToFinance();
        }
    }

    /**
     * Navigates to the FinanceActivity and finishes this activity so it is removed from the back stack.
     */
    private void navigateToFinance() {
        Intent intent = new Intent(this, FinanceActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Copies the database file from the assets folder to the device's internal storage.
     * This is necessary so that the app can access the database for read/write operations.
     */
    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {
            assetNames = assetManager.list(DB_PATH);
            if (assetNames != null) {
                for (int i = 0; i < assetNames.length; i++) {
                    assetNames[i] = DB_PATH + "/" + assetNames[i];
                }
                copyAssetsToDirectory(assetNames, dataDirectory);
                Main.setDBPathName(dataDirectory.toString() + "/" + Main.getDBPathName());
            }

        } catch (Exception e) {
            Log.e("Error", "Cannot Copy database");
        }
    }

    /**
     * Copies the specified asset files to the target directory on the device.
     *
     * @param assets    Array of asset file paths to copy.
     * @param directory Target directory on the device where assets will be copied.
     * @throws IOException if an error occurs during copying.
     */
    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset: assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length-1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if ( !outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer,0,count);
                    count = in.read(buffer);
                }
                out.close();
                in.close();
            }
        }
    }
}