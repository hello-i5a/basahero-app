package com.example.basaheroapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.UploadProcess.AddBookDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditBook extends AppCompatActivity {

    private TextInputLayout textInputLayoutTitle, textInputLayoutAuthor, textInputLayoutGenre, textInputLayoutDate;
    private TextInputEditText editTextTitle, editTextAuthor, editTextGenre, editTextDate, descriptionEditText;
    private boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_book);

        String bookID = getIntent().getStringExtra("bookid");

        textInputLayoutTitle = findViewById(R.id.edit_title_layout);
        textInputLayoutAuthor = findViewById(R.id.edit_author_layout);
        textInputLayoutGenre = findViewById(R.id.edit_genre_layout);
        textInputLayoutDate = findViewById(R.id.edit_date_layout);

        editTextTitle = findViewById(R.id.edit_title);
        editTextAuthor = findViewById(R.id.edit_author);
        editTextGenre = findViewById(R.id.edit_genre);
        editTextDate = findViewById(R.id.edit_date);
        descriptionEditText = findViewById(R.id.edit_desc);

        setValues(bookID);

        editTextTitle.addTextChangedListener(new EditBook.ValidationTextWatcher(editTextTitle));
        editTextAuthor.addTextChangedListener(new EditBook.ValidationTextWatcher(editTextAuthor));
        editTextGenre.addTextChangedListener(new EditBook.ValidationTextWatcher(editTextGenre));
        editTextDate.addTextChangedListener(new EditBook.ValidationTextWatcher(editTextDate));
        descriptionEditText.addTextChangedListener(new EditBook.ValidationTextWatcher(descriptionEditText));


        Button edit = findViewById(R.id.confirmedit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    Python py = Python.getInstance();
                    py.getModule("storage").callAttr("updatePostedBook", bookID, editTextTitle.getText().toString().trim(), editTextAuthor.getText().toString().trim(),
                            editTextGenre.getText().toString().trim(), editTextDate.getText().toString().trim(), descriptionEditText.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "Book Edited Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void setValues(String bookID) {
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("storage").callAttr("getPostedBook",bookID);

        String input = pyObject.toString();
        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String booksData = input.substring(startIndex, endIndex);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(booksData);
            JSONObject book = jsonArray.getJSONObject(0);
            System.out.println(book);

            editTextTitle.setText(book.getString("title"));
            editTextAuthor.setText(book.getString("author"));
            editTextGenre.setText(book.getString("genre"));
            editTextDate.setText(book.getString("pub_date"));
            descriptionEditText.setText(book.getString("description"));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fucking idiot", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

        // Check if all fields are valid
        if (TextUtils.isEmpty(editTextTitle.getText().toString().trim())) {
            isValid = false;
        } else {
            isValid = true;
        }
        if (TextUtils.isEmpty(editTextAuthor.getText().toString().trim())) {
            isValid = false;
        } else {
            isValid = true;
        }
        if (TextUtils.isEmpty(editTextGenre.getText().toString().trim())) {
            isValid = false;
        } else {
            isValid = true;
        }
        if (TextUtils.isEmpty(editTextDate.getText().toString().trim())) {
            isValid = false;
        } else {
            isValid = true;
        }

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Enable/Disable the button based on character count
                if (charSequence.length() > 500) {
                    isValid = false;  // Disable button if length exceeds 500
                } else {
                    isValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed here
            }
        });

    }
}