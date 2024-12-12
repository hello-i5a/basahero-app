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
import android.widget.Toast;

import com.example.basaheroapp.Utilities.Database;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class Register extends Fragment {

    private TextInputLayout regemail_layout, reguser_layout, regname_layout;
    private TextInputEditText reguser, regemail, regname;
    private ProgressBar loadingProgressBar;
    private Button signInButton;
    String name, email, user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Button
        signInButton = view.findViewById(R.id.register);
        loadingProgressBar = view.findViewById(R.id.loading_progress);
        ColorStateList tintColor = ColorStateList.valueOf(Color.parseColor("#4A90E2")); // Blue color
        signInButton.setBackgroundTintList(tintColor);

        regemail_layout = view.findViewById(R.id.regemail_layout);
        reguser_layout = view.findViewById(R.id.reguser_layout);
        regname_layout = view.findViewById(R.id.regname_layout);
        reguser = view.findViewById(R.id.reguser);
        regemail = view.findViewById(R.id.regemail);
        regname = view.findViewById(R.id.regname);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = regname.getText().toString().trim();
                user = reguser.getText().toString().trim();
                email = regemail.getText().toString().trim();
                boolean check = true;

                if (name.isEmpty()) {
                    regname_layout.setError("Name is required");
                    check = false;
                } else {
                    regname_layout.setError(null); // Clear any previous error
                }

                if (user.isEmpty()) {
                    reguser_layout.setError("Username is required");
                    check = false;
                } else {
                    reguser_layout.setError(null); // Clear any previous error
                }

                if (email.isEmpty()) {
                    regemail_layout.setError("Email is required");
                    check = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // Check if email matches the regex pattern for a valid email
                    regemail_layout.setError("Invalid email format");
                    check = false;
                } else {
                    regemail_layout.setError(null);
                }

                if (check) {
                    signInButton.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    checkUsername(user, email);
                }
            }
        });

        return view;
    }

    private void checkUsername(String username, String email) {
        // Create a Database instance and perform login
        Database db = new Database();
        db.checkAccountUsername(username, new Database.CheckCallback() {
            @Override
            public void onResult(int auth) {
                boolean chk;
                if (auth != 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reguser_layout.setError("Username Already Taken!");
                        }
                    });
                    chk = false;
                } else {
                    chk = true;
                }

                checkEmail(email, chk);
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

    private void checkEmail(String email, boolean booluser) {
        // Create a Database instance and perform login
        Database db = new Database();
        db.checkAccountEmail(email, new Database.CheckCallback() {
            @Override
            public void onResult(int auth) {
                if (auth != 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            regemail_layout.setError("Email Already Taken!");
                        }
                    });
                } else {
                    if (booluser) {
                        Intent intent = new Intent(getContext(), Password.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        signInButton.setVisibility(View.VISIBLE);
                        loadingProgressBar.setVisibility(View.GONE);
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