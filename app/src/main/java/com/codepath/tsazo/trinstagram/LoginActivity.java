package com.codepath.tsazo.trinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A login screen that offers login via username/password
 */

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // First check if user is already logged in
        if (ParseUser.getCurrentUser() != null)
            goMainActivity();


        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Login button clicked");
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(username, password);
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Signup button clicked");
                goSignupActivity();
            }
        });

    }


    // Checks if the user has entered in the proper login credentials, if so, user is taken to the MainActivity
    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);

        // logInInBackground preferred to execute this login in the background thread
        // rather than the main thread (helps UX)
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    // TODO: better error handling
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO: navigate to main activity if the user has signed in properly
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Creates a flow (using Intents) to the MainActivity
    private void goMainActivity() {
        // Intent(this context, activity I want to navigate to)
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // Goes to SignupActivity on click
    private void goSignupActivity() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }
}
