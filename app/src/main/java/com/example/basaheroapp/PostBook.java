package com.example.basaheroapp;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.basaheroapp.UploadProcess.AddBookDescription;
import com.example.basaheroapp.UploadProcess.AddBookDetails;
import com.example.basaheroapp.UploadProcess.ReviewDetails;
import com.example.basaheroapp.UploadProcess.UploadBookImage;
import com.example.basaheroapp.Utilities.Database;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PostBook extends AppCompatActivity {

    private int progress = 0;

    private Uri bookImg;

    private String posttitle, postauthor, postgenre, postdate, postdesc, postfile_name, accID;

    public String getPostgenre() {
        return postgenre;
    }

    public void setPostgenre(String postgenre) {
        this.postgenre = postgenre;
    }

    public String getPostfile_name() {
        return postfile_name;
    }

    public void setPostfile_name(String postfile_name) {
        this.postfile_name = postfile_name;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostauthor() {
        return postauthor;
    }

    public void setPostauthor(String postauthor) {
        this.postauthor = postauthor;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPostdesc() {
        return postdesc;
    }

    public void setPostdesc(String postdesc) {
        this.postdesc = postdesc;
    }

    public Uri getBookImg() {
        return bookImg;
    }

    public void setBookImg(Uri bookImg) {
        this.bookImg = bookImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_book);

        accID = getIntent().getStringExtra("accID");

        Button back = findViewById(R.id.upload_back);
        back.setVisibility(View.GONE);
        Button next = findViewById(R.id.upload_next);
        TabLayout tabs = findViewById(R.id.upload_progress);

        if (savedInstanceState == null) {  // Avoid adding the fragment multiple times
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.upload_frame, new UploadBookImage());
            transaction.commit();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (progress) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new AddBookDetails()).commit();
                        progress++;
                        back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new AddBookDescription()).commit();
                        progress++;
                        break;

                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new ReviewDetails()).commit();
                        progress++;
                        next.setText("Post!");
                        break;
                    case 3:
                        uploadBookImage();
                        finish();
                        break;

                }
                tabs.getTabAt(progress).select();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (progress) {
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new UploadBookImage()).commit();
                        progress--;
                        back.setVisibility(View.GONE);
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new AddBookDetails()).commit();
                        progress--;
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_frame, new AddBookDescription()).commit();
                        progress--;
                        next.setText("Next");
                        break;
                }
                tabs.getTabAt(progress).select();
            }
        });

    }

    public void uploadBookImage() {
        ContentResolver contentResolver = getContentResolver();
        Bitmap bitmap;
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, bookImg);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, bookImg);
                bitmap = ImageDecoder.decodeBitmap(source);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);  // You can change to JPEG if needed
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            Python py = Python.getInstance();
            py.getModule("storage").callAttr("uploadImg", byteArray, postfile_name, posttitle, postauthor, postgenre, postdate, postdesc, accID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}