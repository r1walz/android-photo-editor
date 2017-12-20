package com.rohit.kt.android_photo_editor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private Intent galIntent;
    private Uri imageUri;
    private final int GALLERY_REQ_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galIntent, "Select Image:"), GALLERY_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                cameraActivity.image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cameraActivity.k = 1;
            finish();
        } else {
            cameraActivity.k = 9;
            finish();
        }

    }
}
