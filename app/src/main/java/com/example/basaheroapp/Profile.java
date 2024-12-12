package com.example.basaheroapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.basaheroapp.Utilities.AccountDetails;
import com.google.android.material.textfield.TextInputEditText;


public class Profile extends Fragment {

    TextInputEditText name, user, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.etFullname);
        user = view.findViewById(R.id.etUsername);
        email = view.findViewById(R.id.etEmail);

        name.setText(AccountDetails.getInstance("").getName());
        user.setText(AccountDetails.getInstance("").getUsername());
        email.setText(AccountDetails.getInstance("").getEmail());

        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditProfile.class);
                intent.putExtra("accid", AccountDetails.getInstance("").getId());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        name.setText(AccountDetails.getInstance("").getName());
        user.setText(AccountDetails.getInstance("").getUsername());
        email.setText(AccountDetails.getInstance("").getEmail());
    }
}