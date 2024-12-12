package com.example.basaheroapp.UploadProcess;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.basaheroapp.PostBook;
import com.example.basaheroapp.R;

import java.io.File;

public class UploadBookImage extends Fragment {

    private ImageView preview;
    private TextView filename;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    String currentPermission;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_book_image, container, false);

        Button next = getActivity().findViewById(R.id.upload_next);
        next.setEnabled(false);

        RelativeLayout gallery = view.findViewById(R.id.upload);
        preview = view.findViewById(R.id.preview);
        filename = view.findViewById(R.id.file_name);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            preview.setImageURI(selectedImage);
                            preview.setAlpha(1f);
                            String fileName = getFileNameFromUri(selectedImage); // Get the file name
                            filename.setText(fileName);
                            ((PostBook)getActivity()).setBookImg(selectedImage);
                            ((PostBook)getActivity()).setPostfile_name(fileName);
                            next.setEnabled(true);
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        preview.setImageURI(imageUri);
                        preview.setAlpha(1f);
                        String fileName = getFileNameFromUri(imageUri); // Get the file name
                        filename.setText(fileName);
                        ((PostBook)getActivity()).setBookImg(imageUri);
                        ((PostBook)getActivity()).setPostfile_name(fileName);
                        next.setEnabled(true);
                    }
                }
        );

        gallery.setOnClickListener(v -> showImageSourceDialog());

        return view;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        if (currentPermission.equals(Manifest.permission.CAMERA)) {
                            Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            imageUri = createImageUri(requireActivity());
                            openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            cameraLauncher.launch(openCamera);
                        } else if (currentPermission.equals(Manifest.permission.READ_EXTERNAL_STORAGE) || currentPermission.equals(Manifest.permission.READ_MEDIA_IMAGES)) {
                            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryLauncher.launch(openGallery);
                        }
                    } else {
                        String permissionMessage = currentPermission.equals(Manifest.permission.CAMERA) ?
                                "Camera permission is required to take photos." :
                                "Gallery permission is required to access photos.";
                        Toast.makeText(getActivity(), permissionMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private Uri createImageUri(Context context) {
        File imageFolder = new File(context.getExternalFilesDir(null), "images");

        // Attempt to create the directory
        boolean directoriesCreated = imageFolder.mkdirs();

        // Log the result for debugging purposes
        if (!directoriesCreated && !imageFolder.exists()) {
            Log.e("ImageUriCreation", "Failed to create directory: " + imageFolder.getAbsolutePath());
            return null; // Return null or handle the error as appropriate
        }

        // Create the file and return its URI
        File file = new File(imageFolder, "captured_image.jpg");
        return FileProvider.getUriForFile(context, "com.example.basaheroapp.provider", file);
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Camera
                            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                currentPermission = Manifest.permission.CAMERA;
                                requestPermissionLauncher.launch(currentPermission);
                            } else {
                                imageUri = createImageUri(requireActivity());
                                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                openCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                cameraLauncher.launch(openCamera);
                            }
                            break;
                        case 1: // Gallery
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                currentPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
                                requestPermissionLauncher.launch(currentPermission);
                            } else {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryLauncher.launch(pickPhoto);
                            }
                            break;
                    }
                });
        builder.create().show();
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        // Check if URI is not null
        if (uri != null) {
            // Query the URI to get the display name (file name)
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    fileName = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("UploadBookImage", "Error getting file name", e);
            }
        }

        return fileName;
    }
}