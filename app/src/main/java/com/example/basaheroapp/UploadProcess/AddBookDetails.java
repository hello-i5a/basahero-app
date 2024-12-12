package com.example.basaheroapp.UploadProcess;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.example.basaheroapp.PostBook;
import com.example.basaheroapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class    AddBookDetails extends Fragment {

    private TextInputLayout textInputLayoutTitle, textInputLayoutAuthor, textInputLayoutGenre, textInputLayoutDate;
    private TextInputEditText editTextTitle, editTextAuthor, editTextGenre, editTextDate;
    private Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_book_detail, container, false);

        next = getActivity().findViewById(R.id.upload_next);
        next.setEnabled(false);

        // Initialize the views
        textInputLayoutTitle = view.findViewById(R.id.post_textInputLayoutTitle);
        textInputLayoutAuthor = view.findViewById(R.id.post_textInputLayoutAuthor);
        textInputLayoutGenre = view.findViewById(R.id.post_textInputLayoutGenre);
        textInputLayoutDate = view.findViewById(R.id.post_textInputLayoutDate);

        editTextTitle = view.findViewById(R.id.post_title);
        editTextAuthor = view.findViewById(R.id.post_author);
        editTextGenre = view.findViewById(R.id.post_genre);
        editTextDate = view.findViewById(R.id.post_date);


        editTextTitle.addTextChangedListener(new ValidationTextWatcher(editTextTitle));
        editTextAuthor.addTextChangedListener(new ValidationTextWatcher(editTextAuthor));
        editTextGenre.addTextChangedListener(new ValidationTextWatcher(editTextGenre));
        editTextDate.addTextChangedListener(new ValidationTextWatcher(editTextDate));

        return view;
    }

    private class ValidationTextWatcher implements TextWatcher {

        private TextInputEditText editText;

        public ValidationTextWatcher(TextInputEditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // No action needed
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
            // No action needed
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Validate the field after text changes
            String text = editable.toString().trim();
            if (TextUtils.isEmpty(text)) {
                setError(editText, "This field is required.");
            } else {
                clearError(editText);
            }

            // After each change, check if all fields are valid
            checkFormValidity();
        }

        private void setError(TextInputEditText editText, String errorMessage) {
            if (editText == editTextTitle) {
                textInputLayoutTitle.setError(errorMessage);
            } else if (editText == editTextAuthor) {
                textInputLayoutAuthor.setError(errorMessage);
            } else if (editText == editTextGenre) {
                textInputLayoutGenre.setError(errorMessage);
            } else if (editText == editTextDate) {
                textInputLayoutDate.setError(errorMessage);
            }
        }

        private void clearError(TextInputEditText editText) {
            if (editText == editTextTitle) {
                textInputLayoutTitle.setError(null);
            } else if (editText == editTextAuthor) {
                textInputLayoutAuthor.setError(null);
            } else if (editText == editTextGenre) {
                textInputLayoutGenre.setError(null);
            } else if (editText == editTextDate) {
                textInputLayoutDate.setError(null);
            }
        }
    }

    // Method to check the validity of the form
    private void checkFormValidity() {
        boolean isValid = true;

        // Check if all fields are valid
        if (TextUtils.isEmpty(editTextTitle.getText().toString().trim())) {
            isValid = false;
        }
        if (TextUtils.isEmpty(editTextAuthor.getText().toString().trim())) {
            isValid = false;
        }
        if (TextUtils.isEmpty(editTextGenre.getText().toString().trim())) {
            isValid = false;
        }
        if (TextUtils.isEmpty(editTextDate.getText().toString().trim())) {
            isValid = false;
        }

        // Enable or disable the button based on the form validity
        if (isValid) {
            ((PostBook)getActivity()).setPosttitle(editTextTitle.getText().toString().trim());
            ((PostBook)getActivity()).setPostauthor(editTextAuthor.getText().toString().trim());
            ((PostBook)getActivity()).setPostgenre(editTextGenre.getText().toString().trim());
            ((PostBook)getActivity()).setPostdate(editTextDate.getText().toString().trim());
        }
        next.setEnabled(isValid);
    }
}