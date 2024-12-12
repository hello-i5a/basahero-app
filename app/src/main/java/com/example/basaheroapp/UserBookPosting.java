package com.example.basaheroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.Utilities.ListAdapter;
import com.example.basaheroapp.Utilities.PostedListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserBookPosting extends Fragment {

    ArrayList<String> imgUrl = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> genre = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Float> ratings = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ListView list;
    PostedListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_book_posting, container, false);

        getData();

        list = view.findViewById(R.id.posted_list);
        listAdapter = new PostedListAdapter(getActivity(), imgUrl, titles, authors, genre, dates, ratings, ids);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.book_id);
                Intent intent = new Intent(getActivity(), PostingDetail.class);
                intent.putExtra("bookid", id.getText());
                startActivity(intent);

            }
        });
        return view;
    }

    public void getData() {
        ids.clear();
        imgUrl.clear();
        titles.clear();
        authors.clear();
        genre.clear();
        dates.clear();

        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("storage").callAttr("getPostedBooksList", ((MainActivity)getActivity()).accountDetails.getId());

        String input = pyObject.toString();
        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String booksData = input.substring(startIndex, endIndex);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(booksData);

            // Loop through the array and assign values to the ArrayLists
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject book = jsonArray.getJSONObject(i);

                // Add values to the ArrayLists
                ids.add(book.getInt("book_id"));
                imgUrl.add(book.getString("img_name"));
                titles.add(book.getString("title"));
                authors.add(book.getString("author"));
                genre.add(book.getString("genre"));
                dates.add(book.getString("pub_date"));

                // Rating may be empty, so handle that
                String ratingValue = book.getString("rating");
                if (ratingValue.isEmpty()) {
                    ratings.add(0.0f);  // If rating is empty, default to 0
                } else {
                    ratings.add(Float.parseFloat(ratingValue));  // Parse the rating as a float
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        listAdapter = new PostedListAdapter(getActivity(), imgUrl, titles, authors, genre, dates, ratings, ids);
        list.setAdapter(listAdapter);
    }
}