package com.example.basaheroapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basaheroapp.Utilities.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class Login extends Fragment {

    private ProgressBar loadingProgressBar;
    private Button signInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Get references to UI elements
        signInButton = view.findViewById(R.id.signin);
        TextView reg = view.findViewById(R.id.reg_text);
        loadingProgressBar = view.findViewById(R.id.loading_progress);
        TextInputEditText usernameEditText = view.findViewById(R.id.login_username);
        TextInputEditText passwordEditText = view.findViewById(R.id.login_password);
        TextInputLayout usernameInputLayout = view.findViewById(R.id.username_input_layout);
        TextInputLayout passwordInputLayout = view.findViewById(R.id.password_input_layout);

        // Set the sign-in button color
        ColorStateList tintColor = ColorStateList.valueOf(Color.parseColor("#4A90E2")); // Blue color
        signInButton.setBackgroundTintList(tintColor);

        // Sign-in button click listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().toLowerCase();
                String password = passwordEditText.getText().toString();
                boolean check = true;

                // Perform input validation
                if (username.isEmpty()) {
                    usernameInputLayout.setError("Username/Email is required");
                    check = false;
                } else {
                    usernameInputLayout.setError(null); // Clear any previous error
                }

                if (password.isEmpty()) {
                    passwordInputLayout.setError("Password is required");
                    check = false;
                } else {
                    passwordInputLayout.setError(null); // Clear any previous error
                }

                if (check) {
                    signInButton.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    login(username, password);
                }
            }
        });

        // Registration text click listener
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to registration tab
                TabLayout tabs = getActivity().findViewById(R.id.tabs);
                tabs.getTabAt(1).select();  // Assuming registration is at index 1
            }
        });

        return view;
    }

    private void login(String username, String password) {
        // Create a Database instance and perform login
        Database db = new Database();
        db.login(username, password, new Database.CheckCallback() {
            @Override
            public void onResult(int auth) {
                switch (auth) {
                    case -1:
                        // Login failed
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 0:
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        // Login successful
                        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) { // Check if email matches the regex pattern for a valid email
                            intent.putExtra("email", username);
                            intent.putExtra("username", "");
                        } else {
                            intent.putExtra("email", "");
                            intent.putExtra("username", username);
                        }
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 1:
                        // Handle the case for other conditions (e.g. blocked account or admin)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:
                        // Handle the case for other conditions (e.g. blocked account or admin)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Account Not Found, Try Signing Up", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Account Not Found, Try Signing Up", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingProgressBar.setVisibility(View.GONE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(IOException e) {
                // Handle network or database failure
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
