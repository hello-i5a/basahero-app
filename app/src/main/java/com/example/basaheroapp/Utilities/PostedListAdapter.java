package com.example.basaheroapp.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basaheroapp.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostedListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private Context context;
    private ArrayList<String> listTitle;
    private ArrayList<String> imgUrl;
    private ArrayList<String> listAuthor;
    private ArrayList<String> listGenre;
    private ArrayList<String> listDate;
    private ArrayList<Float> listRating;
    private ArrayList<Integer> id;

    // Constructor to initialize the Adapter with ArrayLists
    public PostedListAdapter(Context context, ArrayList<String> imgUrl, ArrayList<String> listTitle, ArrayList<String> listAuthor, ArrayList<String> genre,
                             ArrayList<String> listDate, ArrayList<Float> listRating, ArrayList<Integer> id) {
        this.context = context;
        this.imgUrl = imgUrl;
        this.listTitle = listTitle;
        this.listAuthor = listAuthor;
        this.listGenre = genre;
        this.listDate = listDate;
        this.listRating = listRating;
        this.id = id;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listTitle.size();  // Use size of the ArrayList
    }

    @Override
    public Object getItem(int i) {
        return null;  // Not needed in this case, as we're using indexes directly
    }

    @Override
    public long getItemId(int i) {
        return i;  // You can return the index of the item, or if not required, return -1
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.posted_book_list, null);
        ImageView img = view.findViewById(R.id.bookimg);
        TextView title = view.findViewById(R.id.list_title);
        TextView author = view.findViewById(R.id.list_author);
        TextView genre = view.findViewById(R.id.list_genre);
        TextView date = view.findViewById(R.id.list_date);
        RatingBar rating = view.findViewById(R.id.list_rating);
        TextView bookID = view.findViewById(R.id.book_id);

        Glide.with(context)
                .load("https://zibrpedawwcozzwcepfg.supabase.co/storage/v1/object/public/book_img/" + imgUrl.get(i))
                .placeholder(R.color.borderColor)
                .into(img);

        // Set the data from ArrayLists to the views
        title.setText(listTitle.get(i));     // Use get() to access list elements
        author.setText(listAuthor.get(i));
        date.setText(listDate.get(i));
        genre.setText(listGenre.get(i));
        rating.setRating(listRating.get(i));  // RatingBar expects a float
        bookID.setText(String.valueOf(id.get(i)));  // Convert Integer to String

        return view;
    }
}
