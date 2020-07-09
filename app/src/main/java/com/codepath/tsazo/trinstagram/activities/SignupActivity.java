package com.codepath.tsazo.trinstagram.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.tsazo.trinstagram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //TODO: Reduce redundant code here - also in LoginActivity
        // Put Instagram photo on the menu_actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.menu_actionbar);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Signup button clicked");
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();
                signupUser(username, password, email);
            }
        });
    }

    // Checks if the user has entered in signup credentials, if so, user is taken to the MainActivity
    private void signupUser(String username, String password, String email) {
        Log.i(TAG, "Attempting to signup user: " + username);

        // Create the ParseUser
        ParseUser user = new ParseUser();

        // Set core properties
        if(username == "" || password == "" || email == "") {
            // TODO: better error handling
            Log.e(TAG, "Not all required fields have been filled out");
            Toast.makeText(SignupActivity.this, "Not all required fields have been filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Set custom properties
        //user.put("phone", "650-253-0000");

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    // TODO: better error handling
                    Log.e(TAG, "Issue with signup", e);
                    Toast.makeText(SignupActivity.this, "Issue with signup!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO: navigate to main activity if the user has signed in properly
                goMainActivity();
                Toast.makeText(SignupActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

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
}