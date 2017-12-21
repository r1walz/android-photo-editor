package com.rohit.kt.android_photo_editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

public class cameraActivity extends AppCompatActivity {

    public static int k = 0;
    //TODO: add variables
    private Intent cameraIntent;
    public static Bitmap image;
    private final int CAMERA_REQUEST_CODE = 1024;
    private File tempFile;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        canvasActivity.drawView = findViewById(R.id.drawing);

        tempFile = new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = FileProvider.getUriForFile(cameraActivity.this, BuildConfig.APPLICATION_ID + ".provider", tempFile);

        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            k = 1;

            finish();
        } else {
            k = 9;
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tempFile.delete();
    }
}
