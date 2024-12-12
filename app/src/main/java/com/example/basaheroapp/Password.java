package com.example.basaheroapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basaheroapp.Utilities.Database;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Password extends AppCompatActivity {

    private TextInputLayout regPassLayout, regConPassLayout;
    private TextInputEditText regPassEditText, regConPassEditText;
    private ProgressBar loadingProgressBar;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        Intent prevIntent = getIntent();
        String name = prevIntent.getStringExtra("name");
        String user = prevIntent.getStringExtra("user");
        String email = prevIntent.getStringExtra("email");

        regPassLayout = findViewById(R.id.regpass_layout);
        regConPassLayout = findViewById(R.id.regconpass_layout);
        regPassEditText = findViewById(R.id.regpass);
        regConPassEditText = findViewById(R.id.regconpass);

        signUpButton = findViewById(R.id.signup);

        signUpButton.setOnClickListener(view -> {
            // Get the entered values
            String password = regPassEditText.getText().toString().trim();
            String confirmPassword = regConPassEditText.getText().toString().trim();

            // Validate password and confirm password
            if (TextUtils.isEmpty(password)) {
                regPassLayout.setError("Password cannot be empty");
            } else {
                regPassLayout.setError(null);
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                regConPassLayout.setError("Confirm Password cannot be empty");
            } else {
                regConPassLayout.setError(null);
            }
            if (!password.equals(confirmPassword)) {
                regConPassLayout.setError("Passwords do not match");
            } else {
                // If all validations pass, proceed with next step
                regPassLayout.setError(null);  // Remove error
                regConPassLayout.setError(null);  // Remove error

                Database db = new Database();

                signUpButton.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Perform network operation here
                        db.register(name, user, email, password);
                    }
                }).start();

                // Continue with the signup process, for example, move to the next activity
                Toast.makeText(getApplicationContext(), "Account Creation Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); // Replace with actual activity
                intent.putExtra("username", user);
                startActivity(intent);

                signUpButton.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.GONE);

                finish();
            }
        });
    }
}