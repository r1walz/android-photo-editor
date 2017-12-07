package com.rohit.kt.android_photo_editor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button canvasButtton;
    private Button cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasButtton = findViewById(R.id.canBtn);
        cameraButton = findViewById(R.id.camBtn);
    }


}
