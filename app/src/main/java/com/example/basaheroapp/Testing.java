package com.example.basaheroapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testing extends AppCompatActivity {

    private Uri selectedImageUri;  // Variable to store the URI of the selected image
    TextView textView;
    Bitmap bitmap;

    // Create an ActivityResultLauncher for the image picker
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    ContentResolver contentResolver = getContentResolver();
                    selectedImageUri = data.getData();  // Get the selected image URI
                    try {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri);
                        } else {
                            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, selectedImageUri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        // Compress the bitmap to JPEG or PNG format and write it to the ByteArrayOutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);  // You can change to JPEG if needed
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        // You can now use the Bitmap (e.g., display it in an ImageView)
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(bitmap);

                        File img = new File(selectedImageUri.getPath());
                        Python py = Python.getInstance();
                        PyObject pyObject = py.getModule("storage").callAttr("uploadImg", byteArray);
                        textView.setText(pyObject.toString());
                    } catch (Exception e) {
                        textView.setText(e.toString());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testing);


        Button btn = findViewById(R.id.btn);
        Button uploadButton = findViewById(R.id.upload_button); // Add a button for uploading image
        textView = findViewById(R.id.output);

        // Button to upload image
        uploadButton.setOnClickListener(view -> {
            // Open the image picker when the upload button is clicked
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

//        btn.setOnClickListener(new V);

    }
}