package com.example.basaheroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.Utilities.ListAdapter;
import com.example.basaheroapp.Utilities.PostedListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WantToRead extends Fragment {

    ArrayList<String> imgUrl = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> genre = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Float> ratings = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ListView list;
    TextView empty;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_want_to_read, container, false);

        list = view.findViewById(R.id.read_want);
        empty = view.findViewById(R.id.empty);
        getData();

        listAdapter = new ListAdapter(getActivity(), imgUrl, titles, authors, genre, dates, ratings, ids);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.book_id);
                Intent intent = new Intent(getActivity(), BookStatusDetails.class);
                intent.putExtra("accid", ((MainActivity) getActivity()).accountDetails.getId());
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
        PyObject pyObject = py.getModule("storage").callAttr("getWantReadList", ((MainActivity) getActivity()).accountDetails.getId());

        String input = pyObject.toString();
        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String booksData = input.substring(startIndex, endIndex);
        System.out.println(booksData);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(booksData);

            if (jsonArray.length() == 0) {
                empty.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            } else {
                empty.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                // Loop through the array and assign values to the ArrayLists
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject book = jsonArray.getJSONObject(i).getJSONObject("books");

                    // Add values to the ArrayLists
                    ids.add(Integer.valueOf(book.getString("book_id")));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        listAdapter = new ListAdapter(getActivity(), imgUrl, titles, authors, genre, dates, ratings, ids);
        list.setAdapter(listAdapter);
    }
}