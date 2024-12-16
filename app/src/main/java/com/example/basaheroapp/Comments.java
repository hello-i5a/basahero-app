package com.example.basaheroapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.Utilities.AccountDetails;
import com.example.basaheroapp.Utilities.CommentsAdapter;
import com.example.basaheroapp.Utilities.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Comments extends AppCompatActivity {

    ArrayList<String> comname = new ArrayList<>();
    ArrayList<String> comcont = new ArrayList<>();
    ArrayList<String> comdate = new ArrayList<>();
    ListView list;
    CommentsAdapter listAdapter;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comments);

        String accid = getIntent().getStringExtra("accid");
        String bookid = getIntent().getStringExtra("bookid");
        String title = getIntent().getStringExtra("title");
        submit = findViewById(R.id.submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        getData(bookid);

        list = findViewById(R.id.commentlist);
        listAdapter = new CommentsAdapter(getApplicationContext(), comname ,comcont, comdate);
        list.setAdapter(listAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar loadingProgressBar = findViewById(R.id.loading_progress);
                submit.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.VISIBLE);

                EditText comment = findViewById(R.id.comment_text);
                String content = comment.getText().toString().trim();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String currentDate = sdf.format(calendar.getTime());

                Python py = Python.getInstance();
                py.getModule("storage").callAttr("insertComments", accid,bookid, content, currentDate);

                comname.add(AccountDetails.getInstance("").getName());
                comcont.add(content);
                comdate.add(currentDate);
                listAdapter.notifyDataSetChanged();

                submit.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

    }

    public void getData(String bookid) {

        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("storage").callAttr("getComments", bookid);
        String input = pyObject.toString();

        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String data = input.substring(startIndex, endIndex);

        System.out.println(data);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(data);

            // Loop through the array and assign values to the ArrayLists
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject response = jsonArray.getJSONObject(i);

                // Add values to the ArrayLists
                comname.add(response.getString("name"));
                comcont.add(response.getString("content"));
                comdate.add(response.getString("created"));
            }

            System.out.println(comname);

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