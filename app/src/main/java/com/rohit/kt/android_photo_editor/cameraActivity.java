package com.rohit.kt.android_photo_editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class cameraActivity extends AppCompatActivity {

    public static int k = 0;
    //TODO: add variables
    private Intent cameraIntent;
    public  static  Bitmap image;
    private final int CAMERA_REQUEST_CODE = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        canvasActivity.drawView = findViewById(R.id.drawing);
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            k=1;
            startActivity(new Intent(cameraActivity.this, canvasActivity.class));
        }
    }
}
