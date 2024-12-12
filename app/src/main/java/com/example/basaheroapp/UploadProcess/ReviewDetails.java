package com.example.basaheroapp.UploadProcess;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.basaheroapp.PostBook;
import com.example.basaheroapp.R;
import com.google.android.material.textfield.TextInputEditText;


public class ReviewDetails extends Fragment {

    private TextView reviewFilename;
    private TextView reviewTitle;
    private TextView reviewAuthor;
    private TextView reviewGenre;
    private TextView reviewDate;
    private TextInputEditText review_desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_details, container, false);

        reviewFilename = rootView.findViewById(R.id.review_filename);
        reviewTitle = rootView.findViewById(R.id.review_title);
        reviewAuthor = rootView.findViewById(R.id.review_author);
        reviewGenre = rootView.findViewById(R.id.review_genre);
        reviewDate = rootView.findViewById(R.id.review_date);
        review_desc = rootView.findViewById(R.id.review_desc);

        review_desc.setFocusableInTouchMode(false);
        review_desc.clearFocus();

        // Assuming you are getting the PostBook object from the activity
        PostBook postBook = (PostBook) getActivity();

        // Set the values from PostBook object to the TextViews
        if (postBook != null) {
            reviewFilename.setText(postBook.getPostfile_name());
            reviewTitle.setText(postBook.getPosttitle());
            reviewAuthor.setText(postBook.getPostauthor());
            reviewGenre.setText(postBook.getPostgenre());  // Assuming this getter exists
            reviewDate.setText(postBook.getPostdate());
            review_desc.setText(postBook.getPostdesc());
        }

        return rootView;
    }
}