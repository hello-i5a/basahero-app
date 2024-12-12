package com.example.basaheroapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.basaheroapp.Utilities.GetBooks;
import com.example.basaheroapp.Utilities.Item;
import com.example.basaheroapp.Utilities.RecyclerViewAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostingDetail extends AppCompatActivity {

    private ImageView imageView, delete;
    private TextView booktitle, bookauthor, bookgenre, bookdate, bookdesc;
    private RatingBar bookrating;
    private String bookID, imgFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posting_detail);

        booktitle = findViewById(R.id.title);
        bookauthor = findViewById(R.id.author);
        bookgenre = findViewById(R.id.genre);
        bookdate = findViewById(R.id.date);
        bookdesc = findViewById(R.id.desc);
        bookrating = findViewById(R.id.rating);
        imageView = findViewById(R.id.detail_img);
        delete = findViewById(R.id.delete);

        bookID = getIntent().getStringExtra("bookid");
        getData(bookID);

        AppBarLayout appbar = findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.col_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appbar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float scrollPercentage = Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();

                // Adjust the alpha of the image based on the scroll percentage
                float x = 0.5F;
                float alphaValue = 1 - (scrollPercentage + x);  // As scroll increases, alpha decreases
                if (scrollPercentage == 0) {
                    alphaValue = 1;
                }

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    collapsingToolbarLayout.setTitle(booktitle.getText());
                }
                else
                {
                    //Expanded
                    collapsingToolbarLayout.setTitle(" ");
                }

                // Set the new alpha value to the image
                imageView.setAlpha(alphaValue);
            }
        });

        RecyclerView rv = findViewById(R.id.detail_recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(testItem());
        rv.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        Button edit = findViewById(R.id.edit);
        ColorStateList tintColor = ColorStateList.valueOf(Color.parseColor("#FF9800"));
        edit.setBackgroundTintList(tintColor);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditBook.class);
                intent.putExtra("bookid", bookID);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(PostingDetail.this, R.style.myDialog));
                dialog.setTitle("Delete this Book");
                dialog.setMessage("Are you sure you want to delete this?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Python py = Python.getInstance();
                        py.getModule("storage").callAttr("deleteBook",bookID, imgFilename);
                        Toast.makeText(getApplicationContext(), "Book Deleted", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
        });
    }

    private ArrayList<Item> testItem() {
        ArrayList<Item> items = new ArrayList<>();

        GetBooks getBooks = new GetBooks();
        String input = getBooks.getNewBooks();
        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String booksData = input.substring(startIndex, endIndex);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(booksData);

            int len = (jsonArray.length() >= 3 ) ? 3 : jsonArray.length();

            // Loop through the array and assign values to the ArrayLists
            for (int i = 0; i < len; i++) {
                JSONObject book = jsonArray.getJSONObject(i);
                items.add(new Item(book.getInt("book_id"), book.getString("title"), book.getString("author"), book.getString("pub_date"), book.getString("description"), book.getString("img_name")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
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

    public void getData(String bookID) {
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
            imgFilename = book.getString("img_name");

            Glide.with(getApplicationContext())
                    .load("https://zibrpedawwcozzwcepfg.supabase.co/storage/v1/object/public/book_img/" + imgFilename)
                    .placeholder(R.color.borderColor)
                    .into(imageView);

            booktitle.setText(book.getString("title"));
            bookauthor.setText(book.getString("author"));
            bookgenre.setText(book.getString("genre"));
            bookdate.setText(book.getString("pub_date"));
            bookdesc.setText(book.getString("description"));

            // Rating may be empty, so handle that
            String ratingValue = book.getString("rating");
            if (ratingValue.isEmpty()) {
                bookrating.setRating(0.0f);  // If rating is empty, default to 0
            } else {
                bookrating.setRating(Float.parseFloat(ratingValue));  // Parse the rating as a float
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(bookID);
    }
}