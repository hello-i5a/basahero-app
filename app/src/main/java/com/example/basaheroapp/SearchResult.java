package com.example.basaheroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.Utilities.AccountDetails;
import com.example.basaheroapp.Utilities.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    ArrayList<String> imgUrl = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> genre = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Float> ratings = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ListView list;
    ListAdapter listAdapter;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);

        keyword = getIntent().getStringExtra("keyword");
        TextView textView = findViewById(R.id.res);
        textView.setText(keyword);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();
        list = findViewById(R.id.newList);
        listAdapter = new ListAdapter(getApplicationContext(), imgUrl, titles, authors, genre, dates, ratings, ids);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = view.findViewById(R.id.book_id);
                Intent intent = new Intent(getApplicationContext(), BookDetail.class);
                intent.putExtra("bookid", id.getText());
                intent.putExtra("accid", AccountDetails.getInstance("").getId());
                startActivity(intent);

            }
        });

    }

    public void getData() {

        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("storage").callAttr("searchBook", keyword);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the "Up" button click
        if (item.getItemId() == android.R.id.home) {
            // Finish the activity when the "Up" button is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}