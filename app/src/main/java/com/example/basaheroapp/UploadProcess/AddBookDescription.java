package com.example.basaheroapp.UploadProcess;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.basaheroapp.PostBook;
import com.example.basaheroapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddBookDescription extends Fragment {

    private TextInputEditText descriptionEditText;
    private Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_book_description, container, false);

        // Initialize the TextInputEditText and Button
        descriptionEditText = rootView.findViewById(R.id.post_date);
        next = getActivity().findViewById(R.id.upload_next);
        next.setEnabled(false);

        // Add a TextWatcher to the description EditText
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Enable/Disable the button based on character count
                if (charSequence.length() > 500) {
                    next.setEnabled(false);  // Disable button if length exceeds 500
                } else {
                    ((PostBook) getActivity()).setPostdesc(descriptionEditText.getText().toString().trim());
                    next.setEnabled(true);  // Enable button if length is within limit
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed here
            }
        });
        return rootView;
    }
}